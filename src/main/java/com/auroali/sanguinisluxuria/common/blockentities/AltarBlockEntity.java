package com.auroali.sanguinisluxuria.common.blockentities;

import com.auroali.sanguinisluxuria.SanguinisLuxuriaClient;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import com.auroali.sanguinisluxuria.common.particles.DelayedParticleEffect;
import com.auroali.sanguinisluxuria.common.registry.SLBlockEntities;
import com.auroali.sanguinisluxuria.common.registry.SLParticles;
import com.auroali.sanguinisluxuria.common.registry.SLRecipeTypes;
import com.auroali.sanguinisluxuria.common.registry.SLSounds;
import com.auroali.sanguinisluxuria.common.rituals.ActiveRitualData;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AltarBlockEntity extends BlockEntity implements ItemDisplayingBlockEntity, EntityTargetingBlockEntity<LivingEntity>, Clearable {
    private static final Vec3d ITEM_OFFSET = new Vec3d(0.5, 0.45, 0.5);
    private static final int INVENTORY_SIZE = 1;
    private static final int PEDESTAL_SEARCH_RADIUS = 8;

    private final SimpleInventory inventory;

    private UUID targetUUID;
    private ActiveRitualData activeRitual;

    private int ritualProcessingTicks;
    private int displayTicks;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(SLBlockEntities.ALTAR, pos, state);
        this.inventory = new SimpleInventory(INVENTORY_SIZE);
        this.inventory.addListener(inventory -> {
            if (this.getWorld() != null && !this.getWorld().isClient) {
                this.markDirty();
                this.getWorld().updateListeners(
                  this.getPos(),
                  this.getCachedState(), this.getCachedState(),
                  Block.NOTIFY_LISTENERS
                );
            }
        });
    }

    public boolean startRitual(World world, BlockPos pos, BlockState state, @Nullable LivingEntity initiator, boolean redstone) {
        if (!redstone && !VampireHelper.isVampire(initiator))
            return false;

        LivingEntity target = this.getTarget();
        if (redstone && target == null)
            return false;

        List<PedestalBlockEntity> nearbyPedestals = new ArrayList<>();
        this.forEachPedestalAround(world, pos, pedestal -> {
            if (!pedestal.getInventory().isEmpty())
                nearbyPedestals.add(pedestal);
        });

        SimpleInventory craftingInventory = new SimpleInventory(1 + nearbyPedestals.size());
        craftingInventory.setStack(0, this.getInventory().getStack(0));
        for (int i = 0; i < nearbyPedestals.size(); i++) {
            craftingInventory.setStack(i + 1, nearbyPedestals.get(i).getInventory().getStack(0));
        }

        Ritual ritual = world.getRecipeManager().getFirstMatch(SLRecipeTypes.ALTAR_RECIPE, craftingInventory, world)
          .map(recipe -> {
              nearbyPedestals.forEach(pedestal -> {
                  ItemStack consumed = pedestal.getInventory().removeStack(0, 1);
                  ItemStack remainder = consumed.getRecipeRemainder();
                  BlockPos pedestalPos = pedestal.getPos();
                  if (!remainder.isEmpty()) {
                      world.spawnEntity(new ItemEntity(
                        world,
                        pedestalPos.getX() + 0.5,
                        pedestalPos.getY() + 1.0,
                        pedestalPos.getZ() + 0.5,
                        remainder
                      ));
                  }
                  RitualUtil.spawnItemConsumedParticlesAt(world, pedestalPos);
              });
              return recipe.getRitual();
          })
          .orElse(null);

        if (ritual != null) {
            this.activeRitual = new ActiveRitualData(
              ritual,
              initiator == null ? target.getUuid() : initiator.getUuid(),
              target == null ? initiator.getUuid() : this.targetUUID
            );
            this.ritualProcessingTicks = 0;
            this.inventory.removeStack(0, 1);
            if (!redstone) {
                this.targetUUID = null;
            }
            world.setBlockState(pos, state
              .with(AltarBlock.ACTIVE, true)
              .with(EntityTargetingBlockEntity.TARGET, target != null)
            );
            return true;
        }

        return false;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRitualProgress() {
        return this.ritualProcessingTicks;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, this.inventory.stacks);
        nbt.putInt("ProcessingTicks", this.ritualProcessingTicks);
        return nbt;
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (this.targetUUID != null)
            nbt.putUuid("Target", this.targetUUID);

        if (this.activeRitual != null) {
            NbtCompound activeRitualTag = new NbtCompound();
            ActiveRitualData.writeNbt(activeRitualTag, this.activeRitual);
            nbt.put("Ritual", activeRitualTag);
        }

        nbt.putInt("ProcessingTicks", this.ritualProcessingTicks);
        Inventories.writeNbt(nbt, this.inventory.stacks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Target"))
            this.targetUUID = nbt.getUuid("Target");

        if (nbt.contains("Ritual", NbtElement.COMPOUND_TYPE)) {
            this.activeRitual = ActiveRitualData.readNbt(nbt.getCompound("Ritual"));
        }

        this.ritualProcessingTicks = nbt.getInt("ProcessingTicks");
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory.stacks);
    }

    @Override
    public void setTarget(LivingEntity entity) {
        this.targetUUID = entity.getUuid();
        this.markDirty();
        if (this.getWorld() != null)
            this.getWorld().setBlockState(this.getPos(), this.getCachedState().with(EntityTargetingBlockEntity.TARGET, true));
    }

    @Override
    public void clearTarget() {
        this.targetUUID = null;
        this.markDirty();
        if (this.getWorld() != null)
            this.getWorld().setBlockState(this.getPos(), this.getCachedState().with(EntityTargetingBlockEntity.TARGET, false));
    }

    @Override
    public LivingEntity getTarget() {
        if (this.targetUUID != null && this.getWorld() instanceof ServerWorld serverWorld) {
            if (serverWorld.getEntity(this.targetUUID) instanceof LivingEntity living)
                return living;
        }
        return null;
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity instanceof LivingEntity && !this.getCachedState().get(AltarBlock.ACTIVE);
    }

    @Override
    public ItemStack getDisplayItem() {
        return this.inventory.getStack(0);
    }

    @Override
    public int getDisplayTicks() {
        return this.displayTicks;
    }

    @Override
    public Vec3d getDisplayOffset() {
        return ITEM_OFFSET;
    }

    public static void tickClient(World world, BlockPos pos, BlockState state, AltarBlockEntity entity) {
        entity.displayTicks++;
        if (state.get(AltarBlock.ACTIVE))
            SanguinisLuxuriaClient.isAltarActive = true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, AltarBlockEntity entity) {
        if (entity.activeRitual == null) {
            return;
        }

        if (entity.ritualProcessingTicks++ < ActiveRitualData.TIME_TO_COMPLETE) {
            if (entity.ritualProcessingTicks % 20 == 0 && world instanceof ServerWorld serverWorld) {
                world.playSound(null, pos, SLSounds.ALTAR_BEATS, SoundCategory.BLOCKS);
                serverWorld.spawnParticles(
                  new DelayedParticleEffect(SLParticles.ALTAR_BEAT, 2),
                  entity.getPos().getX() + 0.5,
                  entity.getPos().getY() + 0.05,
                  entity.getPos().getZ() + 0.5,
                  0,
                  0,
                  0,
                  0,
                  0
                );
            }
            entity.markDirty();
            return;
        }

        Ritual ritual = entity.activeRitual.ritual();
        LivingEntity initiator = entity.activeRitual.resolveInitiator(world);
        LivingEntity target = entity.activeRitual.resolveTarget(world);
        if (initiator == null) {
            entity.activeRitual = null;
            entity.ritualProcessingTicks = 0;
            entity.markDirty();
            world.setBlockState(pos, state
              .with(AltarBlock.ACTIVE, false)
              .with(EntityTargetingBlockEntity.TARGET, entity.targetUUID != null)
            );
            return;
        }

        ritual.onCompleted(
          RitualParameters
            .builder()
            .initiator(initiator)
            .target(target)
            .position(pos)
            .world(world)
            .inventory(entity.inventory)
            .build()
        );

        entity.activeRitual = null;
        entity.ritualProcessingTicks = 0;
        entity.markDirty();
        world.setBlockState(pos, state
          .with(AltarBlock.ACTIVE, false)
          .with(EntityTargetingBlockEntity.TARGET, entity.targetUUID != null)
        );
    }

    protected void forEachPedestalAround(World world, BlockPos pos, Consumer<? super PedestalBlockEntity> consumer) {
        BlockPos min = new BlockPos(pos.getX() - PEDESTAL_SEARCH_RADIUS, pos.getY() - PEDESTAL_SEARCH_RADIUS, pos.getZ() - PEDESTAL_SEARCH_RADIUS);
        BlockPos max = new BlockPos(pos.getX() + PEDESTAL_SEARCH_RADIUS, pos.getY() + PEDESTAL_SEARCH_RADIUS, pos.getZ() + PEDESTAL_SEARCH_RADIUS);
        Box bounds = new Box(min, max);
        int chunkMinX = ChunkSectionPos.getSectionCoord(min.getX());
        int chunkMaxX = ChunkSectionPos.getSectionCoord(max.getX());
        int chunkMinZ = ChunkSectionPos.getSectionCoord(min.getZ());
        int chunkMaxZ = ChunkSectionPos.getSectionCoord(max.getZ());

        for (int x = chunkMinX; x <= chunkMaxX; x++) {
            for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
                if (!world.isChunkLoaded(x, z))
                    continue;
                WorldChunk chunk = world.getChunk(x, z);
                chunk.getBlockEntities().forEach((bPos, be) -> {
                    if (be instanceof PedestalBlockEntity pedestalBlockEntity && bounds.contains(bPos.getX(), bPos.getY(), bPos.getZ()))
                        consumer.accept(pedestalBlockEntity);
                });
            }
        }
    }

    @Override
    public void clear() {
        this.targetUUID = null;
        this.activeRitual = null;
        this.inventory.clear();
    }
}

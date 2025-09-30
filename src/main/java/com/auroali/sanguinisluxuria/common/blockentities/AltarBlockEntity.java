package com.auroali.sanguinisluxuria.common.blockentities;

import com.auroali.sanguinisluxuria.SanguinisLuxuriaClient;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import com.auroali.sanguinisluxuria.common.network.packets.AltarRecipeStartS2C;
import com.auroali.sanguinisluxuria.common.particles.DelayedParticleEffect;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.common.rituals.ActiveRitualData;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AltarBlockEntity extends BlockEntity implements Inventory, ItemDisplayingBlockEntity {
    private static final Vec3d ITEM_OFFSET = new Vec3d(0.5, 0.45, 0.5);
    public static final int INVENTORY_SIZE = 1;
    public static final int PEDESTAL_SEARCH_RADIUS = 8;

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    protected int ticks;
    private LivingEntity cachedInitiator;
    private LivingEntity cachedTarget;
    private ActiveRitualData ritualData;
    private int ticksProcessing;
    private UUID storedTarget;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(SLBlockEntities.ALTAR, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);

        if (nbt.containsUuid("StoredTarget"))
            this.storedTarget = nbt.getUuid("StoredTarget");
        this.ritualData = ActiveRitualData.readNbt(nbt);
        this.ticksProcessing = nbt.getInt("TicksProcessing");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);

        if (this.storedTarget != null)
            nbt.putUuid("StoredTarget", this.storedTarget);
        ActiveRitualData.writeNbt(nbt, this.ritualData);
        nbt.putInt("TicksProcessing", this.ticksProcessing);
    }

    public static void tickClient(World world, BlockPos pos, BlockState state, AltarBlockEntity altar) {
        altar.ticks++;
        if (state.get(AltarBlock.ACTIVE))
            SanguinisLuxuriaClient.isAltarActive = true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, AltarBlockEntity altar) {
        if (altar.ritualData == null)
            return;

        LivingEntity initiator = altar.getInitiator();
        LivingEntity target = altar.getTarget();
        if (!VampireHelper.isVampire(initiator) || target == null) {
            altar.ritualData = null;
            altar.ticksProcessing = 0;
            world.setBlockState(pos, state.with(AltarBlock.ACTIVE, false).with(AltarBlock.TARGET, false));
            altar.markDirty();
            return;
        }

        if (world.getTime() % 20 == 0 && altar.getWorld() instanceof ServerWorld serverWorld) {
            world.playSound(null, pos, SLSounds.ALTAR_BEATS, SoundCategory.BLOCKS);
            serverWorld.spawnParticles(
              new DelayedParticleEffect(SLParticles.ALTAR_BEAT, 2),
              altar.getPos().getX() + 0.5,
              altar.getPos().getY() + 0.05,
              altar.getPos().getZ() + 0.5,
              0,
              0,
              0,
              0,
              0
            );
        }

        if (altar.ticksProcessing < ActiveRitualData.TIME_TO_COMPLETE) {
            altar.ticksProcessing++;
            altar.markDirty();
            return;
        }

        Ritual ritual = altar.ritualData.ritual();
        RitualParameters parameters = RitualParameters
          .builder()
          .world(world)
          .position(pos)
          .inventory(altar)
          .initiator(initiator)
          .target(target)
          .build();
        ritual.onCompleted(parameters);
        parameters.applyToPlayerInitiator(player -> SLAdvancementCriterion.PERFORM_RITUAL.trigger(player, ritual, parameters));
        altar.getStack(0).decrement(1);
        altar.ritualData = null;
        altar.ticksProcessing = 0;
        altar.cachedTarget = null;
        altar.cachedInitiator = null;
        world.setBlockState(pos, state.with(AltarBlock.ACTIVE, false).with(AltarBlock.TARGET, false));
        altar.markDirty();
    }

    public void startRitual(World world, LivingEntity initiator, BlockPos pos, BlockState state) {
        if (!VampireHelper.isVampire(initiator))
            return;

        List<BlockPos> pedestalPositions = new ArrayList<>();
        List<ItemStack> pedestalItems = new ArrayList<>();

        BlockPos.streamOutwards(pos, PEDESTAL_SEARCH_RADIUS, PEDESTAL_SEARCH_RADIUS, PEDESTAL_SEARCH_RADIUS)
          .map(position -> world.getBlockEntity(position, SLBlockEntities.PEDESTAL))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(pedestal -> {
              if (pedestal.getItem().isEmpty())
                  return;

              pedestalPositions.add(pedestal.getPos());
              pedestalItems.add(pedestal.getItem().copy());
          });

        SimpleInventory inventory = new SimpleInventory(pedestalItems.size() + 1);
        inventory.setStack(0, this.getStack(0));
        for (int i = 0; i < pedestalItems.size(); i++) {
            inventory.setStack(i + 1, pedestalItems.get(i));
        }

        world.getRecipeManager().getFirstMatch(SLRecipeTypes.ALTAR_RECIPE, inventory, world)
          .ifPresent(recipe -> {
              // send vfx packet
              AltarRecipeStartS2C packet = new AltarRecipeStartS2C(pos, pedestalPositions);
              PlayerLookup.tracking(this)
                .forEach(player -> ServerPlayNetworking.send(player, packet));

              pedestalPositions.forEach(position -> {
                  PedestalBlockEntity entity = ((PedestalBlockEntity) world.getBlockEntity(position));
                  if (entity == null)
                      return;
                  // spawn the item remainder, if there are any
                  ItemStack stack = entity.getItem().split(1);
                  ItemStack remainder = stack.getRecipeRemainder();
                  if (!remainder.isEmpty()) {
                      ItemEntity itemEntity = new ItemEntity(
                        world,
                        position.getX() + 0.5,
                        position.getY() + 1.0,
                        position.getZ() + 0.5,
                        remainder
                      );
                      world.spawnEntity(itemEntity);
                  }
                  entity.getInventory().markDirty();
              });

              this.ticksProcessing = 0;
              boolean storedTargetAlive = this.isStoredTargetAlive(this.world);
              this.ritualData = new ActiveRitualData(
                recipe.getRitual(),
                initiator.getUuid(),
                storedTargetAlive ? this.storedTarget : initiator.getUuid()
              );
              world.setBlockState(pos, state.with(AltarBlock.ACTIVE, true).with(AltarBlock.TARGET, storedTargetAlive));

              if (initiator instanceof ServerPlayerEntity player) {
                  Criteria.RECIPE_CRAFTED.trigger(player, recipe.getId(), inventory.stacks);
              }
              this.clearNextTarget();
          });
    }

    public LivingEntity getInitiator() {
        if (this.cachedInitiator == null || !this.cachedInitiator.isAlive()) {
            this.cachedInitiator = null;
            if (this.ritualData != null)
                return this.cachedInitiator = this.ritualData.resolveInitiator(this.getWorld());
            return null;
        }
        return this.cachedInitiator;
    }

    public LivingEntity getTarget() {
        if (this.cachedTarget == null || !this.cachedTarget.isAlive()) {
            this.cachedTarget = null;
            if (this.ritualData != null)
                return this.cachedTarget = this.ritualData.resolveTarget(this.getWorld());
            return null;
        }
        return this.cachedTarget;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        Inventories.writeNbt(compound, this.inventory);
        return compound;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        if (this.inventory.isEmpty())
            return true;
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    public void onInventoryChanged() {
        this.ritualData = null;
        this.markDirty();
        if (this.world != null)
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), AltarBlock.NOTIFY_LISTENERS);
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(this.inventory, slot, amount);
        if (!stack.isEmpty())
            this.onInventoryChanged();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = this.inventory.get(slot);
        this.inventory.set(slot, ItemStack.EMPTY);
        if (!stack.isEmpty())
            this.onInventoryChanged();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        this.onInventoryChanged();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.onInventoryChanged();
    }


    @Override
    public ItemStack getDisplayItem() {
        return this.inventory.get(0);
    }

    @Override
    public int getDisplayTicks() {
        return this.ticks;
    }

    @Override
    public Vec3d getDisplayOffset() {
        return ITEM_OFFSET;
    }

    public void setNextTarget(LivingEntity entity) {
        if (entity.isAlive()) {
            this.storedTarget = entity.getUuid();
            if (this.world != null)
                this.world.setBlockState(this.pos, this.getCachedState().with(AltarBlock.TARGET, true));
            this.markDirty();
        }
    }

    public void clearNextTarget() {
        this.cachedTarget = null;
        this.storedTarget = null;
        this.markDirty();
    }

    private boolean isStoredTargetAlive(World world) {
        if (world instanceof ServerWorld serverWorld && this.storedTarget != null)
            return serverWorld.getEntity(this.storedTarget) instanceof LivingEntity living && living.isAlive();
        return false;
    }

    public int getRecipeTicks() {
        return this.ticksProcessing;
    }
}

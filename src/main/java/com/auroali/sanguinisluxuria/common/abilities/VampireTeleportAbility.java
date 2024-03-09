package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.auroali.sanguinisluxuria.common.registry.BLVampireAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;

import java.util.function.Supplier;

public class VampireTeleportAbility extends VampireAbility{
    public VampireTeleportAbility(Supplier<ItemStack> icon, VampireAbility parent) {
        super(icon, parent);
    }

    @Override
    public void tick(LivingEntity entity, VampireComponent component, BloodComponent blood) {

    }

    @Override
    public boolean isKeybindable() {
        return true;
    }

    @Override
    public boolean activate(LivingEntity entity, VampireComponent component) {
        if(component.getAbilties().isOnCooldown(this))
            return false;

        BlockHitResult result = entity.world.raycast(new RaycastContext(
                entity.getEyePos(),
                entity.getEyePos().add(entity.getRotationVector().multiply(getRange(component.getAbilties()))),
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                entity
        ));

        if(result == null)
            return false;

        BlockPos pos = result.getBlockPos().offset(result.getSide());

        entity.teleport(pos.getX() + 0.5f, result.getPos().getY(), pos.getZ() + 0.5f);
        entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

        component.getAbilties().setCooldown(this, getCooldown(component.getAbilties()));
        return true;
    }

    public double getRange(VampireAbilityContainer container) {
        double range = 6;
        for(VampireAbility ability : container) {
            if(ability.isIn(BLTags.VampireAbilities.TELEPORT_RANGE))
                range += 4;
        }
        return range;
    }
    public int getCooldown(VampireAbilityContainer container) {
        int cooldown = 300;
        if(container.hasAbility(BLVampireAbilities.TELEPORT_COOLDOWN_1))
            cooldown -= 75;
        if(container.hasAbility(BLVampireAbilities.TELEPORT_COOLDOWN_2))
            cooldown -= 75;
        return cooldown;
    }
    @Override
    public boolean canTickCooldown(LivingEntity entity, VampireComponent vampireComponent) {
        return entity.isOnGround();
    }
}

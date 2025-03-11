package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.common.registry.BLEntityAttributes;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);

    @ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
    public Multimap<EntityAttribute, EntityAttributeModifier> sanguinisluxuria$modifyAttributeModifiers(Multimap<EntityAttribute, EntityAttributeModifier> original, @Local(argsOnly = true) EquipmentSlot slot) {
        if (slot != EquipmentSlot.HEAD || !this.isIn(BLTags.Items.SUN_BLOCKING_HELMETS) || original.containsKey(BLEntityAttributes.SUN_RESISTANCE))
            return original;

        return ImmutableMultimap
          .<EntityAttribute, EntityAttributeModifier>builder()
          .putAll(original)
          .put(BLEntityAttributes.SUN_RESISTANCE, new EntityAttributeModifier("Sun Resistance", 0.75d, EntityAttributeModifier.Operation.MULTIPLY_BASE))
          .build();
    }
}

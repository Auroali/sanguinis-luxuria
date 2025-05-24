package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.types.*;
import net.minecraft.registry.Registry;

public class SLRitualTypes {
    public static final RitualType<?> ITEM_RITUAL_TYPE = RitualType.fromCodec(ItemRitual.CODEC);
    public static final RitualType<?> ABILITY_RITUAL_TYPE = RitualType.fromCodec(VampireAbilityRitual.CODEC);
    public static final RitualType<?> ABILITY_RESET_RITUAL_TYPE = RitualType.fromCodec(VampireAbilityResetRitual.CODEC);
    public static final RitualType<?> ABILITY_REVEAL_RITUAL_TYPE = RitualType.fromCodec(AbilityRevealRitual.CODEC);
    public static final RitualType<?> ENTITY_SPAWNING_RITUAL_TYPE = RitualType.fromCodec(EntitySpawningRitual.CODEC);
    public static final RitualType<?> STATUS_EFFECT_RITUAL_TYPE = RitualType.fromCodec(StatusEffectRitual.CODEC);
    public static final RitualType<?> CONVERT_ENTITY_RITUAL = RitualType.fromCodec(ConvertEntityRitual.CODEC);

    public static void register() {
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.ITEM_RITUAL_TYPE, ITEM_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.ABILITY_RITUAL_TYPE, ABILITY_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.ABILITY_RESET_RITUAL_TYPE, ABILITY_RESET_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.ABILITY_REVEAL_RITUAL_TYPE, ABILITY_REVEAL_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.ENTITY_SPAWNING_RITUAL_TYPE, ENTITY_SPAWNING_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.STATUS_EFFECT_RITUAL_TYPE, STATUS_EFFECT_RITUAL_TYPE);
        Registry.register(SLRegistries.RITUAL_TYPES, SLResources.CONVERT_ENTITY_RITUAL_TYPE, CONVERT_ENTITY_RITUAL);
    }
}

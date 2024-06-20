package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class BLSounds {
    public static final SoundEvent DRAIN_BLOOD = SoundEvent.of(BLResources.BLOOD_DRAIN_SOUND);
    public static final SoundEvent ALTAR_BEATS = SoundEvent.of(BLResources.ALTAR_BEATS_SOUND);
    public static final SoundEvent BLEEDING = SoundEvent.of(BLResources.BLEEDING_SOUND);
    public static final SoundEvent VAMPIRE_CONVERT = SoundEvent.of(BLResources.VAMPIRE_CONVERT_SOUND);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, BLResources.BLOOD_DRAIN_SOUND, DRAIN_BLOOD);
        Registry.register(Registries.SOUND_EVENT, BLResources.ALTAR_BEATS_SOUND, ALTAR_BEATS);
        Registry.register(Registries.SOUND_EVENT, BLResources.BLEEDING_SOUND, BLEEDING);
        Registry.register(Registries.SOUND_EVENT, BLResources.VAMPIRE_CONVERT_SOUND, VAMPIRE_CONVERT);
    }
}

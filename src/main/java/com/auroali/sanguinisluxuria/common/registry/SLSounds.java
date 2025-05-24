package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class SLSounds {
    public static final SoundEvent DRAIN_BLOOD = SoundEvent.of(SLResources.BLOOD_DRAIN_SOUND);
    public static final SoundEvent ALTAR_BEATS = SoundEvent.of(SLResources.ALTAR_BEATS_SOUND);
    public static final SoundEvent BLEEDING = SoundEvent.of(SLResources.BLEEDING_SOUND);
    public static final SoundEvent VAMPIRE_CONVERT = SoundEvent.of(SLResources.VAMPIRE_CONVERT_SOUND);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, SLResources.BLOOD_DRAIN_SOUND, DRAIN_BLOOD);
        Registry.register(Registries.SOUND_EVENT, SLResources.ALTAR_BEATS_SOUND, ALTAR_BEATS);
        Registry.register(Registries.SOUND_EVENT, SLResources.BLEEDING_SOUND, BLEEDING);
        Registry.register(Registries.SOUND_EVENT, SLResources.VAMPIRE_CONVERT_SOUND, VAMPIRE_CONVERT);
    }
}

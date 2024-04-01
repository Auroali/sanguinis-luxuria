package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class BLSounds {
    public static final SoundEvent DRAIN_BLOOD = new SoundEvent(BLResources.BLOOD_DRAIN_SOUND);
    public static final SoundEvent ALTAR_BEATS = new SoundEvent(BLResources.ALTAR_BEATS_SOUND);
    public static final SoundEvent BLEEDING = new SoundEvent(BLResources.BLEEDING_SOUND);
    public static final SoundEvent VAMPIRE_CONVERT = new SoundEvent(BLResources.VAMPIRE_CONVERT_SOUND);

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, BLResources.BLOOD_DRAIN_SOUND, DRAIN_BLOOD);
        Registry.register(Registry.SOUND_EVENT, BLResources.ALTAR_BEATS_SOUND, ALTAR_BEATS);
        Registry.register(Registry.SOUND_EVENT, BLResources.BLEEDING_SOUND, BLEEDING);
        Registry.register(Registry.SOUND_EVENT, BLResources.VAMPIRE_CONVERT_SOUND, VAMPIRE_CONVERT);
    }
}

package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class BLSounds {
    public static final SoundEvent DRAIN_BLOOD = new SoundEvent(BLResources.BLOOD_DRAIN_SOUND);

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, BLResources.BLOOD_DRAIN_SOUND, DRAIN_BLOOD);
    }
}

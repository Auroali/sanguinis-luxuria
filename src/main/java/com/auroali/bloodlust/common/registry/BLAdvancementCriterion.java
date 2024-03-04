package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.common.advancements.BecomeVampireCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class BLAdvancementCriterion {
    public static final BecomeVampireCriterion BECOME_VAMPIRE = new BecomeVampireCriterion();

    public static void register() {
        Criteria.register(BECOME_VAMPIRE);
    }
}

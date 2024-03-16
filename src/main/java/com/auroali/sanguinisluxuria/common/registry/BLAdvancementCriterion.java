package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.advancements.BecomeVampireCriterion;
import com.auroali.sanguinisluxuria.common.advancements.ResetAbilitiesCriterion;
import com.auroali.sanguinisluxuria.common.advancements.UnlockAbilityCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class BLAdvancementCriterion {
    public static final BecomeVampireCriterion BECOME_VAMPIRE = new BecomeVampireCriterion();
    public static final UnlockAbilityCriterion UNLOCK_ABILITY = new UnlockAbilityCriterion();
    public static final ResetAbilitiesCriterion RESET_ABILITIES = new ResetAbilitiesCriterion();

    public static void register() {
        Criteria.register(BECOME_VAMPIRE);
        Criteria.register(UNLOCK_ABILITY);
        Criteria.register(RESET_ABILITIES);
    }
}

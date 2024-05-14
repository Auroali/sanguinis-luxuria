package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.advancements.*;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BLAdvancementsProvider extends FabricAdvancementProvider {
    public BLAdvancementsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement becomeVampire = Advancement.Builder
                .create()
                .display(
                        BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), 1),
                        Text.translatable(title("become_vampire")),
                        Text.translatable(desc("become_vampire")),
                        new Identifier("textures/block/redstone_block.png"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("convert", BecomeVampireCriterion.Conditions.create())
                .build(BLResources.id("become_vampire"));
        Advancement unbecomeVampire = Advancement.Builder
                .create()
                .display(
                        BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), 1),
                        Text.translatable(title("unbecome_vampire")),
                        Text.translatable(desc("unbecome_vampire")),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(becomeVampire)
                .criterion("unconvert", UnbecomeVampireCriterion.Conditions.create())
                .build(BLResources.id("unbecome_vampire"));

        Advancement bloodSickness = Advancement.Builder
                .create()
                .display(
                        Items.ROTTEN_FLESH,
                        Text.translatable(title("blood_sickness")),
                        Text.translatable(desc("blood_sickness")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(becomeVampire)
                .criterion("get_blood_sickness", EffectsChangedCriterion.Conditions.create(
                        EntityEffectPredicate.create().withEffect(BLStatusEffects.BLOOD_SICKNESS)
                ))
                .build(BLResources.id("blood_sickness"));

        Advancement infectOther = Advancement.Builder
                .create()
                .display(
                        Items.WITHER_ROSE,
                        Text.translatable(title("infect_other")),
                        Text.translatable(desc("infect_other")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(bloodSickness)
                .criterion("give_blood_sickness", InfectEntityCriterion.Conditions.create())
                .build(BLResources.id("infect_other"));

        Advancement drinkTwistedBlood = Advancement.Builder
                .create()
                .display(
                        BLItems.TWISTED_BLOOD,
                        Text.translatable(title("drink_twisted_blood")),
                        Text.translatable(desc("drink_twisted_blood")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("drink_twisted_blood", ConsumeItemCriterion.Conditions.item(BLItems.TWISTED_BLOOD))
                .parent(becomeVampire)
                .build(BLResources.id("drink_twisted_blood"));

        Advancement unlockAnyAbility = Advancement.Builder
                .create()
                .display(
                        BLBlocks.SKILL_UPGRADER,
                        Text.translatable(title("unlock_ability")),
                        Text.translatable(desc("unlock_ability")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(drinkTwistedBlood)
                .criterion("unlock_ability", UnlockAbilityCriterion.Conditions.create())
                .build(BLResources.id("unlock_ability"));

        Advancement transferEffects = Advancement.Builder
                .create()
                .display(
                        PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                        Text.translatable(title("transfer_effects")),
                        Text.translatable(desc("transfer_effects")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(unlockAnyAbility)
                .criterion("transfer_effects", TransferEffectsCriterion.Conditions.create())
                .build(BLResources.id("transfer_effects"));

        Advancement transferMoreEffects = Advancement.Builder
                .create()
                .display(
                        PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
                        Text.translatable(title("transfer_more_effects")),
                        Text.translatable(desc("transfer_more_effects")),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(transferEffects)
                .criterion("transfer_effects", TransferEffectsCriterion.Conditions.create(4))
                .build(BLResources.id("transfer_more_effects"));

        Advancement resetAbilities = Advancement.Builder
                .create()
                .display(
                        BLItems.BLESSED_BLOOD,
                        Text.translatable(title("reset_abilities")),
                        Text.translatable(desc("reset_abilities")),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(unlockAnyAbility)
                .criterion("reset_abilities", ResetAbilitiesCriterion.Conditions.create())
                .build(BLResources.id("reset_abilities"));

        consumer.accept(becomeVampire);
        consumer.accept(bloodSickness);
        consumer.accept(drinkTwistedBlood);
        consumer.accept(unlockAnyAbility);
        consumer.accept(resetAbilities);
        consumer.accept(infectOther);
        consumer.accept(transferEffects);
        consumer.accept(transferMoreEffects);
        consumer.accept(unbecomeVampire);
    }

    public static String title(String name) {
        return "advancements.%s.%s.title".formatted(Bloodlust.MODID, name.replaceAll("/", "."));
    }
    public static String desc(String name) {
        return "advancements.%s.%s.description".formatted(Bloodlust.MODID, name.replaceAll("/", "."));
    }

}

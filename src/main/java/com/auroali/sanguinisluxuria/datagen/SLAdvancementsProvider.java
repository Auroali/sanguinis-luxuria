package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.advancements.*;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.common.rituals.RitualPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualFieldsPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualTypePredicate;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class SLAdvancementsProvider extends FabricAdvancementProvider {
    public SLAdvancementsProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement getBloodlustEffect = Advancement.Builder
          .create()
          .display(
            SLItems.VAMPIRE_FANG,
            Text.translatable(title("receive_bloodlust")),
            Text.translatable(desc("receive_bloodlust")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .criterion("status_effect_non_vampire", EffectsChangedCriterion.Conditions.create(
            EntityEffectPredicate.create()
              .withEffect(SLStatusEffects.BLOOD_LUST)
          ))
          .build(SLResources.id("receive_bloodlust"));

        Advancement becomeVampire = Advancement.Builder
          .create()
          .display(
            BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE, 1),
            Text.translatable(title("become_vampire")),
            Text.translatable(desc("become_vampire")),
            new Identifier("textures/block/redstone_block.png"),
            AdvancementFrame.CHALLENGE,
            true,
            true,
            false
          )
          .parent(getBloodlustEffect)
          .criterion("convert", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .build(SLResources.id("become_vampire"));

        Advancement craftHungrySapling = Advancement.Builder
          .create()
          .parent(becomeVampire)
          .display(
            new ItemStack(SLBlocks.GRAFTED_SAPLING),
            Text.translatable(title("craft_hungry_sapling")),
            Text.translatable(desc("craft_hungry_sapling")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .criterion("has_item", InventoryChangedCriterion.Conditions.items(SLBlocks.GRAFTED_SAPLING))
          .build(SLResources.id("craft_hungry_sapling"));

        Advancement growDecayedTree = Advancement.Builder
          .create()
          .parent(craftHungrySapling)
          .display(
            new ItemStack(SLBlocks.DECAYED_LOG),
            Text.translatable(title("grow_decayed_tree")),
            Text.translatable(desc("grow_decayed_tree")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .criterion("has_item", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(SLTags.Items.DECAYED_LOGS).build()))
          .build(SLResources.id("grow_decayed_tree"));

        Advancement obtainHungryLog = Advancement.Builder
          .create()
          .parent(growDecayedTree)
          .display(
            new ItemStack(SLBlocks.DECAYED_LOG),
            Text.translatable(title("obtain_hungry_decayed_log")),
            Text.translatable(desc("obtain_hungry_decayed_log")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .criterion("has_item", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(SLTags.Items.HUNGRY_DECAYED_LOGS).build()))
          .build(SLResources.id("obtain_hungry_decayed_log"));

        Advancement unbecomeVampire = Advancement.Builder
          .create()
          .display(
            Items.GLASS_BOTTLE,
            Text.translatable(title("unbecome_vampire")),
            Text.translatable(desc("unbecome_vampire")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .parent(becomeVampire)
          .criterion("unconvert", ConvertCriterion.Conditions.create(ConversionContext.Conversion.DECONVERTING))
          .build(SLResources.id("unbecome_vampire"));

        Advancement purifyOther = Advancement.Builder.create()
          .display(
            PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), SLStatusEffects.BLESSED_WATER_POTION),
            Text.translatable(title("purify_other")),
            Text.translatable(desc("purify_other")),
            null,
            AdvancementFrame.CHALLENGE,
            true,
            true,
            true
          )
          .parent(unbecomeVampire)
          .criterion("ritual", PerformRitualCriterion.Conditions.create(
            RitualPredicate.builder()
              .targetExcluding(EntityPredicate.Builder.create()
                .type(EntityType.PLAYER)
                .build()
              )
              .type(RitualTypePredicate.create(SLRitualTypes.CONVERT_ENTITY_RITUAL))
              .fields(RitualFieldsPredicate.builder()
                .field("conversion", "deconverting")
                .build()
              )
              .build()
          ))
          .build(SLResources.id("purify_other"));

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
          .parent(becomeVampire)
          .criterion("give_blood_sickness", InfectEntityCriterion.Conditions.create())
          .build(SLResources.id("infect_other"));

        Advancement drinkTwistedBlood = Advancement.Builder
          .create()
          .display(
            SLItems.TWISTED_BLOOD,
            Text.translatable(title("drink_twisted_blood")),
            Text.translatable(desc("drink_twisted_blood")),
            null,
            AdvancementFrame.TASK,
            true,
            true,
            false
          )
          .criterion("drink_twisted_blood", ConsumeItemCriterion.Conditions.item(SLItems.TWISTED_BLOOD))
          .parent(growDecayedTree)
          .build(SLResources.id("drink_twisted_blood"));

        Advancement unlockAnyAbility = Advancement.Builder
          .create()
          .display(
            SLBlocks.ALTAR,
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
          .build(SLResources.id("unlock_ability"));

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
          .build(SLResources.id("transfer_effects"));

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
          .build(SLResources.id("transfer_more_effects"));

        Advancement transferTheMostEffects = Advancement.Builder
          .create()
          .display(
            Items.BUCKET,
            Text.translatable(title("transfer_all_effects")),
            Text.translatable(desc("transfer_all_effects")),
            null,
            AdvancementFrame.CHALLENGE,
            true,
            true,
            false
          )
          .parent(transferMoreEffects)
          .criterion("all_effects", TransferEffectsCriterion.Conditions.create(
            EntityEffectPredicate.create()
              .withEffect(StatusEffects.SPEED)
              .withEffect(StatusEffects.SLOWNESS)
              .withEffect(StatusEffects.STRENGTH)
              .withEffect(StatusEffects.JUMP_BOOST)
              .withEffect(StatusEffects.REGENERATION)
              .withEffect(StatusEffects.FIRE_RESISTANCE)
              .withEffect(StatusEffects.WATER_BREATHING)
              .withEffect(StatusEffects.INVISIBILITY)
              .withEffect(StatusEffects.NIGHT_VISION)
              .withEffect(StatusEffects.WEAKNESS)
              .withEffect(StatusEffects.WITHER)
              .withEffect(StatusEffects.HASTE)
              .withEffect(StatusEffects.MINING_FATIGUE)
              .withEffect(StatusEffects.LEVITATION)
              .withEffect(StatusEffects.GLOWING)
              .withEffect(StatusEffects.ABSORPTION)
              .withEffect(StatusEffects.HUNGER)
              .withEffect(StatusEffects.NAUSEA)
              .withEffect(StatusEffects.RESISTANCE)
              .withEffect(StatusEffects.SLOW_FALLING)
              .withEffect(StatusEffects.CONDUIT_POWER)
              .withEffect(StatusEffects.DOLPHINS_GRACE)
              .withEffect(StatusEffects.BLINDNESS)
              .withEffect(StatusEffects.BAD_OMEN)
              .withEffect(StatusEffects.HERO_OF_THE_VILLAGE)
              .withEffect(StatusEffects.DARKNESS)
          ))
          .build(SLResources.id("transfer_all_effects"));

        Advancement resetAbilities = Advancement.Builder
          .create()
          .display(
            SLItems.BLOOD_PETAL,
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
          .build(SLResources.id("reset_abilities"));

        consumer.accept(becomeVampire);
        consumer.accept(drinkTwistedBlood);
        consumer.accept(unlockAnyAbility);
        consumer.accept(resetAbilities);
        consumer.accept(infectOther);
        consumer.accept(transferEffects);
        consumer.accept(transferMoreEffects);
        consumer.accept(transferTheMostEffects);
        consumer.accept(unbecomeVampire);
        consumer.accept(purifyOther);
        consumer.accept(craftHungrySapling);
        consumer.accept(growDecayedTree);
        consumer.accept(obtainHungryLog);
        consumer.accept(getBloodlustEffect);

        generateUnlockAdvancements(consumer);
    }

    // generates advancements for book unlocks
    private static void generateUnlockAdvancements(Consumer<Advancement> consumer) {
        consumer.accept(
          Advancement.Builder.create()
            .criterion("become_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
            .criterion("grow_tree", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(SLTags.Items.DECAYED_LOGS).build()))
            .build(SLResources.id("unlock/grow_tree_and_become_vampire"))
        );
    }

    public static String title(String name) {
        return "advancements.%s.%s.title".formatted(SanguinisLuxuria.MODID, name.replaceAll("/", "."));
    }

    public static String desc(String name) {
        return "advancements.%s.%s.description".formatted(SanguinisLuxuria.MODID, name.replaceAll("/", "."));
    }

    private NbtCompound buildNbt(Consumer<NbtCompound> builder) {
        NbtCompound tag = new NbtCompound();
        builder.accept(tag);
        return tag;
    }
}

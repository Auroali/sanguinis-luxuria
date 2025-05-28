package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.advancements.ConvertCriterion;
import com.auroali.sanguinisluxuria.common.advancements.UnlockAbilityCriterion;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.registry.SLItems;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.common.registry.SLVampireAbilities;
import com.auroali.sanguinisluxuria.common.rituals.types.*;
import com.auroali.sanguinisluxuria.datagen.builders.BloodCauldronFillRecipeJsonBuilder;
import com.auroali.sanguinisluxuria.datagen.builders.BloodCauldronRecipeJsonBuilder;
import com.auroali.sanguinisluxuria.datagen.builders.RitualRecipeJsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.server.recipe.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import java.util.function.Consumer;

public class SLRecipeProvider extends FabricRecipeProvider {
    public SLRecipeProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        this.generateCraftingRecipes(exporter);
        this.generateFurnaceRecipes(exporter);
        this.generateSingleItemRecipes(exporter);
        this.generateCauldronInfusingRecipes(exporter);
        this.generateRitualRecipes(exporter);
    }

    public void generateFurnaceRecipes(Consumer<RecipeJsonProvider> exporter) {
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(SLItems.RAW_SILVER), RecipeCategory.MISC, SLItems.SILVER_INGOT, 0.35f, 200)
          .group("smelting")
          .criterion("has_item", conditionsFromItem(SLItems.RAW_SILVER))
          .offerTo(exporter, SLResources.id("smelting/silver_ingot"));
        CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(SLItems.RAW_SILVER), RecipeCategory.MISC, SLItems.SILVER_INGOT, 0.35f, 100)
          .group("blasting")
          .criterion("has_item", conditionsFromItem(SLItems.RAW_SILVER))
          .offerTo(exporter, SLResources.id("blasting/silver_ingot"));
    }

    public void generateSingleItemRecipes(Consumer<RecipeJsonProvider> exporter) {
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(SLTags.Items.DECAYED_LOGS), RecipeCategory.TOOLS, SLItems.MASK_1)
          .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
          .offerTo(exporter);
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(SLTags.Items.DECAYED_LOGS), RecipeCategory.TOOLS, SLItems.MASK_2)
          .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
          .offerTo(exporter);
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(SLTags.Items.DECAYED_LOGS), RecipeCategory.TOOLS, SLItems.MASK_3)
          .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
          .offerTo(exporter);
    }

    public void generateCraftingRecipes(Consumer<RecipeJsonProvider> exporter) {
        generateFamily(exporter, SLBlockFamilies.DECAYED_WOOD_FAMILY);
        offerPlanksRecipe(exporter, SLItems.DECAYED_PLANKS, SLTags.Items.DECAYED_LOGS, 4);
        offerHangingSignRecipe(exporter, SLItems.DECAYED_HANGING_SIGN, SLItems.STRIPPED_DECAYED_LOG);
        offerBarkBlockRecipe(exporter, SLItems.DECAYED_WOOD, SLItems.DECAYED_LOG);
        offerBarkBlockRecipe(exporter, SLItems.STRIPPED_DECAYED_WOOD, SLItems.STRIPPED_DECAYED_LOG);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, SLItems.GRAFTED_SAPLING)
          .input(SLItems.BLOOD_PETAL)
          .input(ItemTags.SAPLINGS)
          .criterion(hasItem(SLItems.BLOOD_PETAL), conditionsFromItem(SLItems.BLOOD_PETAL))
          .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, SLItems.SILVER_PRESSURE_PLATE)
          .pattern("##")
          .input('#', SLTags.Items.SILVER_INGOTS)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, SLItems.ALTAR)
          .pattern("lbl")
          .pattern("sss")
          .input('b', SLItems.BLOOD_BOTTLE)
          .input('s', SLItems.DECAYED_PLANKS)
          .input('l', SLTags.Items.DECAYED_LOGS)
          .criterion("is_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .criterion("has_blackstone", conditionsFromItem(Items.BLACKSTONE))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, SLItems.PEDESTAL)
          .pattern("l")
          .pattern("l")
          .pattern("l")
          .input('l', SLItems.DECAYED_PLANKS)
          .criterion("has_decayed_plans", conditionsFromItem(SLItems.DECAYED_PLANKS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SLItems.SILVER_SWORD)
          .pattern("I")
          .pattern("I")
          .pattern("S")
          .input('I', SLTags.Items.SILVER_INGOTS)
          .input('S', Items.STICK)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SLItems.SILVER_PICKAXE)
          .pattern("III")
          .pattern(" S ")
          .pattern(" S ")
          .input('I', SLTags.Items.SILVER_INGOTS)
          .input('S', Items.STICK)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SLItems.SILVER_AXE)
          .pattern("II")
          .pattern("IS")
          .pattern(" S")
          .input('I', SLTags.Items.SILVER_INGOTS)
          .input('S', Items.STICK)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SLItems.SILVER_SHOVEL)
          .pattern("I")
          .pattern("S")
          .pattern("S")
          .input('I', SLTags.Items.SILVER_INGOTS)
          .input('S', Items.STICK)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, SLItems.SILVER_HOE)
          .pattern("II")
          .pattern(" S")
          .pattern(" S")
          .input('I', SLTags.Items.SILVER_INGOTS)
          .input('S', Items.STICK)
          .criterion("has_item", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, SLItems.SILVER_INGOT, 9)
          .input(SLItems.SILVER_BLOCK)
          .criterion("has_silver", conditionsFromItem(SLItems.SILVER_BLOCK))
          .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, SLItems.SILVER_BLOCK)
          .input(SLItems.SILVER_INGOT, 9)
          .criterion("has_silver", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, SLItems.RAW_SILVER, 9)
          .input(SLItems.RAW_SILVER_BLOCK)
          .criterion("has_silver", conditionsFromItem(SLItems.RAW_SILVER))
          .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, SLItems.RAW_SILVER_BLOCK)
          .input(SLItems.RAW_SILVER, 9)
          .criterion("has_silver", conditionsFromItem(SLItems.RAW_SILVER))
          .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, SLItems.SILVER_BARS)
          .pattern("###")
          .pattern("###")
          .input('#', SLTags.Items.SILVER_INGOTS)
          .criterion("has_silver", conditionsFromTag(SLTags.Items.SILVER_INGOTS))
          .offerTo(exporter);
    }

    public void generateCauldronInfusingRecipes(Consumer<RecipeJsonProvider> exporter) {
        BloodCauldronRecipeJsonBuilder.create(RecipeCategory.BREWING, Ingredient.fromTag(ItemTags.FLOWERS), SLItems.BLOOD_PETAL)
          .criterion("become_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter);
        BloodCauldronFillRecipeJsonBuilder.create(RecipeCategory.BREWING, Ingredient.ofItems(Items.GLASS_BOTTLE), SLItems.BLOOD_BOTTLE)
          .criterion("has_item", conditionsFromItem(SLItems.BLOOD_BOTTLE))
          .offerTo(exporter);
        BloodCauldronFillRecipeJsonBuilder.create(RecipeCategory.BREWING, SLItems.BLOOD_BAG)
          .criterion("has_item", conditionsFromItem(SLItems.BLOOD_BAG))
          .offerTo(exporter);
        BloodCauldronRecipeJsonBuilder.create(RecipeCategory.BREWING, Ingredient.ofItems(Items.GUNPOWDER), Items.REDSTONE)
          .criterion(hasItem(Items.GUNPOWDER), conditionsFromItem(Items.GUNPOWDER))
          .offerTo(exporter);
    }

    public void generateRitualRecipes(Consumer<RecipeJsonProvider> exporter) {
        RitualRecipeJsonBuilder.create(RecipeCategory.BREWING, ItemRitual.create(SLItems.TWISTED_BLOOD))
          .catalyst(SLItems.BLOOD_BOTTLE)
          .input(Items.NETHER_WART)
          .input(Items.FERMENTED_SPIDER_EYE)
          .input(SLItems.BLOOD_PETAL)
          .criterion("is_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter);
        RitualRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRitual.create(SLItems.BLOOD_BAG))
          .catalyst(Items.GLASS_BOTTLE)
          .input(Items.GLASS)
          .input(Items.GLASS)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter);
        RitualRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemRitual.create(SLItems.PENDANT_OF_PIERCING))
          .catalyst(Items.ARROW)
          .input(SLItems.TWISTED_BLOOD)
          .input(Items.GOLD_INGOT)
          .input(Items.STRING)
          .criterion("unlock_abilities", UnlockAbilityCriterion.Conditions.create(SLVampireAbilities.TELEPORT))
          .offerTo(exporter);

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.TELEPORT))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.ENDER_PEARL)
          .input(Items.CHORUS_FRUIT)
          .input(Items.ENDER_PEARL)
          .input(Items.CHORUS_FRUIT)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/blink"));
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.BITE))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(SLItems.VAMPIRE_FANG)
          .input(Items.BONE)
          .input(SLItems.VAMPIRE_FANG)
          .input(Items.BONE)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/bite"));
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.INFECTIOUS))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.GLASS_BOTTLE)
          .input(Items.GUNPOWDER)
          .input(Items.DRAGON_BREATH)
          .input(Items.GLOWSTONE_DUST)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/infectious"));
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.MIST))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.DRAGON_BREATH)
          .input(Items.COBWEB)
          .input(Items.DRAGON_BREATH)
          .input(Items.COBWEB)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/mist"));
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.RESILIENCE))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.OBSIDIAN)
          .input(Items.OBSIDIAN)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/resilience"));
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new VampireAbilityRitual(SLVampireAbilities.VULNERABILITY))
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.GLASS_PANE)
          .input(Items.GLASS_PANE)
          .criterion("has_twisted_blood", conditionsFromItem(SLItems.TWISTED_BLOOD))
          .offerTo(exporter, SLResources.id("rituals/vulnerability"));

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, VampireAbilityResetRitual.INSTANCE)
          .catalyst(PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLESSED_WATER_POTION))
          .input(Items.SUNFLOWER)
          .input(Items.SUNFLOWER)
          .criterion("unlocked_ability", UnlockAbilityCriterion.Conditions.create())
          .offerTo(exporter, SLResources.id("rituals/reset_abilities"));

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, AbilityRevealRitual.INSTANCE)
          .catalyst(Items.WRITABLE_BOOK)
          .criterion("unlocked_ability", UnlockAbilityCriterion.Conditions.create())
          .offerTo(exporter, SLResources.id("rituals/reveal_abilities"));

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, new ConvertEntityRitual(ConversionContext.Conversion.DECONVERTING))
          .catalyst(Items.GOLDEN_APPLE)
          .input(SLItems.SILVER_INGOT)
          .input(SLItems.SILVER_INGOT)
          .input(SLItems.SILVER_INGOT)
          .criterion("became_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter, SLResources.id("rituals/purification"));

        // effects
        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, StatusEffectRitual.builder().addEffect(StatusEffects.FIRE_RESISTANCE).duration(1900).build())
          .catalyst(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER))
          .input(Items.MAGMA_CREAM)
          .input(Items.MAGMA_CREAM)
          .criterion(hasItem(Items.MAGMA_CREAM), conditionsFromItem(Items.MAGMA_CREAM))
          .criterion("became_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter, SLResources.id("rituals/lesser_fire_resistance"));

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, StatusEffectRitual.builder().addEffect(StatusEffects.FIRE_RESISTANCE).target(StatusEffectRitual.Target.ALL).duration(3600).build())
          .catalyst(SLItems.TWISTED_BLOOD)
          .input(Items.MAGMA_CREAM)
          .input(Items.NETHER_WART)
          .input(Items.MAGMA_CREAM)
          .input(Items.NETHER_WART)
          .criterion(hasItem(SLItems.TWISTED_BLOOD), conditionsFromItem(SLItems.TWISTED_BLOOD))
          .criterion(hasItem(Items.MAGMA_CREAM), conditionsFromItem(Items.MAGMA_CREAM))
          .criterion("became_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter, SLResources.id("rituals/greater_fire_resistance"));

        RitualRecipeJsonBuilder.create(RecipeCategory.MISC, StatusEffectRitual.builder().addEffect(StatusEffects.RESISTANCE).duration(1200).build())
          .catalyst(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER))
          .input(Items.IRON_CHESTPLATE)
          .input(ConventionalItemTags.IRON_INGOTS)
          .input(ConventionalItemTags.IRON_INGOTS)
          .criterion(hasItem(Items.IRON_CHESTPLATE), conditionsFromItem(Items.IRON_CHESTPLATE))
          .criterion("became_vampire", ConvertCriterion.Conditions.create(ConversionContext.Conversion.CONVERTING))
          .offerTo(exporter, SLResources.id("rituals/lesser_resistance"));
    }
}

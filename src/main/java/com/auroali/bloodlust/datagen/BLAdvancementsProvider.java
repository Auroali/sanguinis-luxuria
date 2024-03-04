package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.Bloodlust;
import com.auroali.bloodlust.common.advancements.BecomeVampireCriterion;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
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
                        Bloodlust.BLOODLUST_TAB.createIcon(),
                        Text.translatable(title("become_vampire")),
                        Text.translatable(desc("become_vampire")),
                        new Identifier("textures/block/redstone_block.png"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("convert", BecomeVampireCriterion.Conditions.create())
                .build(BLResources.id("become_vampire"));
        consumer.accept(becomeVampire);
    }

    public static String title(String name) {
        return "advancements.%s.%s.title".formatted(Bloodlust.MODID, name.replaceAll("/", "."));
    }
    public static String desc(String name) {
        return "advancements.%s.%s.description".formatted(Bloodlust.MODID, name.replaceAll("/", "."));
    }

}

package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AbilityRevealRitual extends ItemRitual {
    public static final ItemStack OUTPUT = new ItemStack(Items.WRITTEN_BOOK);
    public static final AbilityRevealRitual INSTANCE = new AbilityRevealRitual();
    public static final Codec<AbilityRevealRitual> CODEC = Codec.unit(() -> INSTANCE);

    protected AbilityRevealRitual() {
        super(OUTPUT, false);
    }

    @Override
    protected ItemStack createResultItem(RitualParameters parameters) {
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(parameters.initiator());
        ItemStack outputStack = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound nbt = outputStack.getOrCreateNbt();
        nbt.putString(WrittenBookItem.AUTHOR_KEY, "Ritual of Revealing");
        nbt.putString(WrittenBookItem.TITLE_KEY, "Transformations");

        // generate the pages for the book
        List<Text> pages = new ArrayList<>();
        int lines = 0;
        for (VampireAbility ability : vampire.getAbilties()) {
            Text abilityText = Text.translatable(ability.getTranslationKey()).formatted(Formatting.DARK_RED, Formatting.BOLD, Formatting.ITALIC);
            if (pages.isEmpty()) {
                pages.add(abilityText);
                lines = 1;
                continue;
            }
            // get the current page
            Text page = pages.get(pages.size() - 1);
            // add the new text to a copy of the page
            Text newPage = Texts.join(List.of(page, abilityText), Text.of("\n"));
            // if the new page can't fit in the book, make a new page
            if (newPage.getString().length() > 1024 || lines > 13) {
                pages.add(abilityText);
                lines = 0;
                continue;
            }
            lines += 1 + abilityText.getString().length() / 114;
            pages.set(pages.size() - 1, newPage);
        }

        // add the pages to the book
        NbtList pagesNbt = new NbtList();
        pages.stream().map(Text.Serializer::toJson).map(NbtString::of).forEach(pagesNbt::add);
        outputStack.setSubNbt(WrittenBookItem.PAGES_KEY, pagesNbt);

        return outputStack;
    }

    @Override
    public RitualType<?> getType() {
        return BLRitualTypes.ABILITY_REVEAL_RITUAL_TYPE;
    }
}

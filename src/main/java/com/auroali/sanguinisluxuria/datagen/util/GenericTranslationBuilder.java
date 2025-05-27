package com.auroali.sanguinisluxuria.datagen.util;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;

import java.util.function.Function;

public class GenericTranslationBuilder<T> {
    private final FabricLanguageProvider.TranslationBuilder builder;
    protected final Function<T, String> keyBuilder;

    public GenericTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder, Function<T, String> keyBuilder) {
        this.builder = builder;
        this.keyBuilder = keyBuilder;
    }

    public void add(T object, String name) {
        this.add(this.keyBuilder.apply(object), name);
    }

    public void add(String key, String name) {
        this.builder.add(key, name);
    }

    public static final class RegistryTranslationBuilder<T> extends GenericTranslationBuilder<T> {
        public RegistryTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder, Registry<T> registry) {
            super(builder, object -> Util.createTranslationKey(registry.getKey().getValue().getPath(), registry.getId(object)));
        }
    }

    public static final class DescriptionTranslationBuilder<T> extends GenericTranslationBuilder<T> {
        private final Function<T, String> descriptionKeyBuilder;

        public DescriptionTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder, Function<T, String> keyBuilder, Function<T, String> descriptionKeyBuilder) {
            super(builder, keyBuilder);
            this.descriptionKeyBuilder = descriptionKeyBuilder;
        }

        public DescriptionTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder, Function<T, String> keyBuilder) {
            this(builder, keyBuilder, object -> keyBuilder.apply(object) + ".desc");
        }

        public void desc(T object, String desc) {
            this.add(this.descriptionKeyBuilder.apply(object), desc);
        }

        public void add(T object, String name, String desc) {
            this.add(object, name);
            this.desc(object, desc);
        }
    }

    public static final class TagTranslationBuilder extends GenericTranslationBuilder<TagKey<?>> {
        public TagTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder) {
            super(builder, tag -> "tag." + tag.registry().getValue().getPath() + "." + tag.id().getNamespace() + "." + tag.id().getPath().replace('/', '.'));
        }
    }

    public static final class Potions extends GenericTranslationBuilder<Potion> {
        private static final Item[] POTIONS = {
          Items.POTION,
          Items.SPLASH_POTION,
          Items.LINGERING_POTION,
          Items.TIPPED_ARROW
        };

        private final String[] prefixes;

        public Potions(FabricLanguageProvider.TranslationBuilder builder, String... prefixes) {
            super(builder, p -> "");
            if (prefixes.length != POTIONS.length)
                throw new IllegalArgumentException("Insufficient number of potion prefixes");
            this.prefixes = prefixes;
        }

        @Override
        public void add(Potion object, String name) {
            for (int i = 0; i < POTIONS.length; i++) {
                this.add(this.getKey(POTIONS[i], object), this.prefixes[i] + name);
            }
        }

        private String getKey(Item item, Potion potion) {
            ItemStack stack = new ItemStack(item);
            PotionUtil.setPotion(stack, potion);
            return stack.getTranslationKey();
        }
    }
}

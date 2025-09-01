package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliColor;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonBook;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonCategory;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliProvider;
import com.auroali.sanguinisluxuria.datagen.patchouli.pages.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;

public class SLPatchouliBooks extends PatchouliProvider {
    public SLPatchouliBooks(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        PatchouliJsonBook book = new PatchouliJsonBook(
                SLResources.id("sanguinisluxuria"),
                "item.sanguinisluxuria.book",
                "sanguinisluxuria.landing",
                "1"
        )
                .model(SLResources.id("book"))
                .texture(PatchouliJsonBook.GUI_BOOK_RED)
                .linkColor(PatchouliColor.fromHex("a51e12"))
                .linkHoverColor(PatchouliColor.fromHex("ff6e11"))
                .macro("$(keybind:", "$(#0066ff)$(l)$(k:")
                .macro("$(blood)", "$(#a51e12)$(l)")
                .group(SLItemGroups.SANGUINIS_LUXURIA_TAB);

        PatchouliJsonBook.LangPack pack = book.createLang("en_us");
        PatchouliJsonCategory cauldronInfusing = pack.category(PatchouliJsonCategory.create(
                "cauldron_infusing",
                "Cauldron Infusing", "Cauldron infusing recipes",
                Items.CAULDRON
        )).sort(1);

        cauldronInfusingEntries(cauldronInfusing);

        PatchouliJsonCategory equipment = pack.category(PatchouliJsonCategory.create(
                "equipment",
                "Equipment", "Items that you may find useful",
                Items.GLASS_BOTTLE
        )).sort(1);

        equipmentEntries(equipment);

        PatchouliJsonCategory intro = pack.category(PatchouliJsonCategory.create(
                "intro",
                "Intro", "An introduction to Sanguinis Luxuria",
                SLItems.BLOOD_BOTTLE
        )).sort(0);

        introEntries(intro);

        PatchouliJsonCategory rituals = pack.category(PatchouliJsonCategory.create(
                "rituals",
                "Rituals", "Rituals performed using the altar",
                SLItems.ALTAR
        )).sort(1);

        ritualEntries(rituals);

        this.register(book);
    }

    private static void cauldronInfusingEntries(PatchouliJsonCategory cauldronInfusing) {
        cauldronInfusing.entry("blood_petal", "Blood Petal", SLItems.BLOOD_PETAL)
                .page(PatchouliTextPage.create("It would seem that drenching a flower in blood destroys all but a single petal. The petal left behind is transformed, infused with blood."))
                .page(PatchouliCauldronInfusingPage.create(SLItems.BLOOD_PETAL))
                .page(PatchouliTextPage.create()
                        .text("A blood petal can be grafted to a sapling, seemingly causing the sapling to quickly wither. Yet the sapling still seems to cling on to life. Water does not satiate it, and it is far too delicate to grow from sunlight...")
                        .text("What would happen if it had a source of blood nearby?"))
                .page(PatchouliCraftingPage.create(SLItems.GRAFTED_SAPLING));
    }

    private static void equipmentEntries(PatchouliJsonCategory equipment) {
        equipment.entry("blessed_water", "Blessed Water", PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLESSED_WATER_POTION))
                .page(PatchouliSpotlightPage.create(PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLESSED_WATER_POTION))
                        .text("Blessed Water can be obtained from max level Clerics. When drank, it grants Blood Protection, which damages vampires whenever they feed on you. When thrown, however, it will not only grant Blood Protection to any living things it hits, it will also damage any undead creatures.")
                )
                .page(PatchouliTextPage.create("When drank by a vampire afflicted with weakness, blessed water will grant them blood protection. Once blood protection runs out, said vampire will be turned human again.")
                        .advancement(SLResources.id("become_vampire"))
                );

        equipment.entry("bottles", "Bottles", Items.GLASS_BOTTLE)
                .page(PatchouliSpotlightPage.create(Items.GLASS_BOTTLE)
                        .text("Bottles are very useful, as they can be used to store blood. Blood can be picked up off the ground with a bottle.")
                )
                .page(PatchouliSpotlightPage.create(BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE))
                        .text("Blood Bottles can fortunately be obtained through more means than blood left on the ground. By pressing $(#a51e12)$(l)$(k:key.sanguinisluxuria.bite)$() while holding an empty bottle and not looking at any living thing with blood, it will take some blood from your blood bar and store it in the bottle.")
                        .text("It does seem like blood bottles have a limited capacity, however, only storing 2 units of blood.")
                        .advancement(SLResources.id("become_vampire"))
                );

        equipment.entry("helmets", "Helmets", Items.LEATHER_HELMET)
                .advancement(SLResources.id("become_vampire"))
                .page(PatchouliSpotlightPage.create(SLTags.Items.SUN_BLOCKING_HELMETS)
                        .text("Certain helmets can grant extra time in the sun before you start to burn. These helmets usually have the trade-off of very little protection, however")
                )
                .page(PatchouliSpotlightPage.create(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(SLEnchantments.SUN_PROTECTION, 1)))
                        .text("Sun Protection is an enchantment that can be applied to any helmet. It provides up to an extra 2 seconds of time in the sun.")
                );

        equipment.entry("pressure_plates", "Pressure Plates", SLItems.SILVER_PRESSURE_PLATE)
                .page(PatchouliSpotlightPage.create(SLItems.SILVER_PRESSURE_PLATE)
                        .title("Silver Pressure Plate")
                        .text("Using silver to create a pressure plate results in a redstone component that will only emit a signal when a living creature steps on it.")
                )
                .page(PatchouliCraftingPage.create(SLItems.SILVER_PRESSURE_PLATE))
                .page(PatchouliSpotlightPage.create(SLItems.DECAYED_PRESSURE_PLATE)
                        .title("Decayed Pressure Plate")
                        .text("Making a pressure plate out of decayed wood, on the other hand, will result in one that only emits a signal when an undead creature steps on it.")
                        .advancement(SLResources.id("grow_decayed_tree"))
                )
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_PRESSURE_PLATE).advancement(SLResources.id("grow_decayed_tree")));

        equipment.entry("silver_tools", "Silver Tools", SLItems.SILVER_SWORD)
                .page(PatchouliSpotlightPage.create(SLItems.SILVER_SWORD, SLItems.SILVER_PICKAXE, SLItems.SILVER_AXE, SLItems.SILVER_SHOVEL, SLItems.SILVER_HOE)
                        .title("Silver Tools")
                        .text("Silver can be used to make tools.")
                        .text("Tools made with silver are on par with tools made of iron normally, but what makes them special is that they deal $(l)increased damage$() against the undead, and are capable of killing vampires.")
                )
                .page(PatchouliCraftingPage.create(SLItems.SILVER_SWORD, SLItems.SILVER_PICKAXE))
                .page(PatchouliCraftingPage.create(SLItems.SILVER_AXE, SLItems.SILVER_SHOVEL))
                .page(PatchouliCraftingPage.create(SLItems.SILVER_HOE));

        equipment.entry("trident", "Tridents", Items.TRIDENT)
                .advancement(SLResources.id("become_vampire"))
                .page(PatchouliSpotlightPage.create(Items.TRIDENT)
                        .text("Tridents can be a rather useful tool for a vampire, due to a certain trident-specific enchantment")
                )
                .page(PatchouliSpotlightPage.create(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(SLEnchantments.BLOOD_TRANSFER, 1)))
                        .title("Blood Transfer")
                        .text(
                                "Blood Transfer allows a trident to latch on to an entity and drain its blood, at the cost of a damage decrease.",
                                "Blood drained by the trident will either go directly into your blood bar, or into a blood-storing item in your hand. Blood Transfer cannot completely drain something's blood, the amount it can drain depends on the level."
                        )
                )
                .page(PatchouliSpotlightPage.create(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(SLEnchantments.SERRATED, 1)))
                        .title("Serrated")
                        .text("Serrated is another trident enchantment. When something is hit with a serrated trident, it has a chance to be inflicted with bleeding. Both the amount of time they bleed for and the chance for bleeding to be inflicted increases with level.")
                );
    }

    private static void introEntries(PatchouliJsonCategory intro) {
        intro.entry("introduction", "Blood", Items.REDSTONE)
                .page(PatchouliTextPage.create()
                        .text("It would seem that killing villagers can sometimes leave some blood behind on the floor. Not only that, but it can be picked up with a bottle; although drinking it provides very little nutritional value and makes you feel more sick with each sip.")
                        .text("I wonder why anyone would want to drink this...")
                );

        intro.entry("blood_sickness", "Blood Sickness", Items.ROTTEN_FLESH)
                .advancement(SLResources.id("blood_sickness"))
                .page(PatchouliTextPage.create(
                        "Blood sickness is the direct result of drinking blood. The more blood you drink, the more sick you get. It seems that at a high enough level it starts poisoning you, perhaps if you let the effect run out something interesting would happen?"
                ));

        intro.entry("altar", "Altar", SLItems.ALTAR)
                .advancement(SLResources.id("unlock/grow_tree_and_become_vampire"))
                .page(PatchouliTextPage.create(
                        "Decayed wood can be used to create an altar capable of performing $(l:sanguinisluxuria:rituals)rituals$(/l)."
                ))
                .page(PatchouliCraftingPage.create(SLItems.ALTAR));

        intro.entry("become_vampire", "Vampires", BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE))
                .advancement(SLResources.id("become_vampire"))
                .page(PatchouliTextPage.create()
                        .text("As a vampire, your body does not accept regular food. You require blood to sustain yourself.")
                        .text("You can obtain blood from most living things, the amount of blood they have depends on their max health. Certain creatures, such as Villagers, provide more blood, while others, such as the undead, provide toxic blood that weakens and hurts you.")
                )
                .page(PatchouliTextPage.create()
                        .text("You can drain blood by being within range of something with blood, and holding $(keybind:key.sanguinisluxuria.bite)$().$")
                        .text("nfortunately, it does seem like Villagers can recognize that you are a vampire, and refuse to even trade with you, let alone go near you. Maybe there's a way around this?")
                );

        intro.entry("decayed_tree", "Decayed Tree", SLItems.DECAYED_LOG)
                .advancement(SLResources.id("grow_decayed_tree"))
                .page(PatchouliTextPage.create(
                        "The blood of nearby animals was enough for the sapling to grow into a decayed tree. The wood that makes up the newly grown tree seems to have a few unique properties that may be useful, but unfortunately the tree itself lacks leaves. It looks like I'll need to make more saplings manually."
                ))
                .page(PatchouliTextPage.create()
                        .text("On occasion, a few logs will grow with holes in them. It seems that these new, hungry logs are capable of draining blood from nearby animals and storing it...")
                        .text("I could probably extract the blood from these, much like a bee's nest.")
                        .advancement(SLResources.id("obtain_hungry_decayed_log"))
                )
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_PLANKS, SLItems.DECAYED_SLAB))
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_STAIRS, SLItems.DECAYED_FENCE))
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_FENCE_GATE, SLItems.DECAYED_DOOR))
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_TRAPDOOR, SLItems.DECAYED_BUTTON))
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_PRESSURE_PLATE, SLItems.DECAYED_SIGN))
                .page(PatchouliCraftingPage.create(SLItems.DECAYED_HANGING_SIGN));

        intro.entry("mask", "Carved Masks", SLItems.MASK_1)
                .advancement(SLResources.id("grow_decayed_tree"))
                .page(PatchouliTextPage.create()
                        .text("A mask, carved from decayed wood. It allows you to hide your face, at the cost of being unable to drain blood.")
                        .text("While worn, Villagers won't run away and Iron Golems won't attack on sight.")
                )
                .page(PatchouliSpotlightPage.create(SLItems.MASK_1)
                        .text("Can be obtained by carving a log in a stonecutter")
                )
                .page(PatchouliSpotlightPage.create(SLItems.MASK_2))
                .page(PatchouliSpotlightPage.create(SLItems.MASK_3));
    }

    private static void ritualEntries(PatchouliJsonCategory rituals) {
        rituals.entry("purification", "Ritual of Purification", Items.GOLDEN_APPLE)
                .advancement(SLResources.id("drink_twisted_blood"))
                .page(PatchouliTextPage.create()
                        .text("The Ritual of Purification is a version of the conversion ritual that acts similarly to curing a Zombie Villager. When performed on a vampire, they will be reverted to their human form.")
                        .text("Unlike curing a Zombie Villager, however, this ritual has a much wider range of uses.")
                )
                .page(PatchouliRitualPage.create(SLResources.id("rituals/purification")));

        rituals.entry("blood_bag", "Blood Bag", SLItems.BLOOD_BAG)
                .advancement(SLResources.id("drink_twisted_blood"))
                .page(PatchouliSpotlightPage.create(SLItems.BLOOD_BAG)
                        .text("Carrying several bottles just to fully satiate your thirst is very inconvenient. The blood bag is a better solution.")
                        .text("It has the capacity of 10 bottles, yet takes up a single slot in your inventory. They can also be filled just like $(l)$(l:sanguinisluxuria:equipment/bottles)glass bottles$()")
                )
                .page(PatchouliRitualPage.create(SLItems.BLOOD_BAG));

        rituals.entry("pendant_of_piercing", "Pendant of Piercing", SLItems.PENDANT_OF_PIERCING)
                .advancement(SLResources.id("drink_twisted_blood"))
                .page(PatchouliSpotlightPage.create(SLItems.PENDANT_OF_PIERCING)
                        .text("When worn, the pendant of piercing turns blink from a tool of escape and mobility into one of offense. Blinking through living creatures while wearing the pendant inflicts damage, but also increases exhaustion.")
                )
                .page(PatchouliRitualPage.create(SLItems.PENDANT_OF_PIERCING));

        rituals.entry("twisted_blood", "Twisted Blood", SLItems.TWISTED_BLOOD)
                .advancement(SLResources.id("unlock/grow_tree_and_become_vampire"))
                .page(PatchouliSpotlightPage.create(SLItems.TWISTED_BLOOD)
                        .text("It would seem blood can be converted into something else. This new, twisted version of blood may have uses as a ritual component...")
                )
                .page(PatchouliRitualPage.create(SLItems.TWISTED_BLOOD));
    }
}

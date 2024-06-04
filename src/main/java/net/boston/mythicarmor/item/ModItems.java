package net.boston.mythicarmor.item;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.item.custom.*;
import net.boston.mythicarmor.item.materials.ModArmorMaterial;
import net.boston.mythicarmor.item.materials.ModToolMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MythicArmor.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> MYTHIC_SHARD = ITEMS.register("mythic_shard", () ->
            new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MYTHIC_INGOT = ITEMS.register("mythic_ingot", () ->
            new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MAGMA_ESSENCE = ITEMS.register("magma_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ENDER_ESSENCE = ITEMS.register("ender_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PROSPERITY_ESSENCE = ITEMS.register("prosperity_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> AMETHYST_ESSENCE = ITEMS.register("amethyst_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> AGILITY_ESSENCE = ITEMS.register("agility_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ANCIENT_ESSENCE = ITEMS.register("ancient_essence", () ->
            new EssenceItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MYTHIC_BOOTS = ITEMS.register("mythic_boots", () ->
            new MythicArmorItem(ModArmorMaterial.MYTHIC, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_LEGGINGS = ITEMS.register("mythic_leggings", () ->
            new MythicArmorItem(ModArmorMaterial.MYTHIC, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_CHESTPLATE = ITEMS.register("mythic_chestplate", () ->
            new MythicArmorItem(ModArmorMaterial.MYTHIC, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_HELMET = ITEMS.register("mythic_helmet", () ->
            new MythicArmorItem(ModArmorMaterial.MYTHIC, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_SWORD = ITEMS.register("mythic_sword", () ->
            new MythicSwordItem(ModToolMaterial.MYTHIC, 8, -2.3f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_PICKAXE = ITEMS.register("mythic_pickaxe", () ->
            new MythicPickaxeItem(ModToolMaterial.MYTHIC, 5, -2.7f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_AXE = ITEMS.register("mythic_axe", () ->
            new MythicAxeItem(ModToolMaterial.MYTHIC, 10, -2.9f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_SHOVEL = ITEMS.register("mythic_shovel", () ->
            new MythicShovelItem(ModToolMaterial.MYTHIC, 6,-2.9f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_HOE = ITEMS.register("mythic_hoe", () ->
            new MythicHoeItem(ModToolMaterial.MYTHIC, 2, 1f, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> MYTHIC_ELYTRA = ITEMS.register("mythic_elytra", () ->
            new MythicElytraItem(new Item.Properties().rarity(Rarity.EPIC).durability(700).fireResistant()));


    private static final List<ResourceLocation> EMPTY_SLOTS = List.of(new ResourceLocation("item/empty_armor_slot_helmet"), new ResourceLocation("item/empty_armor_slot_chestplate"),
            new ResourceLocation("item/empty_armor_slot_leggings"), new ResourceLocation("item/empty_armor_slot_boots"), new ResourceLocation("item/empty_slot_axe"),
            new ResourceLocation("item/empty_slot_sword"), new ResourceLocation("item/empty_slot_shovel"), new ResourceLocation("item/empty_slot_pickaxe"));
    public static final RegistryObject<Item> MYTHIC_UPGRADE = ITEMS.register("mythic_upgrade", () ->
            new SmithingTemplateItem(Component.literal("§8Netherite Equipment, Elytra"), Component.literal("§8Mythic Ingot"), Component.literal("§8Mythic Upgrade"),
                    Component.literal("Add Netherite Armor, Weapon, or Tool, or Elytra"), Component.literal("Add Mythic Ingot"), EMPTY_SLOTS, List.of(new ResourceLocation("item/empty_slot_ingot"))));


    public static final RegistryObject<Item>[] ESSENCES_ARR = new RegistryObject[]{MAGMA_ESSENCE, ENDER_ESSENCE, PROSPERITY_ESSENCE, AMETHYST_ESSENCE, AGILITY_ESSENCE, ANCIENT_ESSENCE};
    public static final RegistryObject<Item>[] MYTHIC_ITEMS_ARR = new RegistryObject[]{
            MYTHIC_HELMET, MYTHIC_CHESTPLATE, MYTHIC_LEGGINGS, MYTHIC_BOOTS, MYTHIC_SWORD, MYTHIC_AXE, MYTHIC_PICKAXE, MYTHIC_SHOVEL, MYTHIC_HOE, MYTHIC_ELYTRA};


    // The order to display the imbued percentages in the item description. Must follow the same order
    // as the meaning of each index in imbueColors array
    public static final String[] imbueNames = {"Magma", "Ender", "Prosperity", "Amethyst", "Agility", "Ancient"};
    // The color of each of the types, in the same order as the other array
    public static final String[] imbueColors = {"§6", "§5", "§a", "§d", "§e", "§1"};
    public static final String[] tagNames = {"imbue.magma", "imbue.ender", "imbue.prosperity", "imbue.amethyst", "imbue.agility", "imbue.ancient"};
    public static final double[][] imbueRGBs = {new double[]{255, 170, 0}, new double[]{170, 0, 170},
                                                new double[]{85, 255, 85}, new double[]{255, 85, 255},
                                                new double[]{255, 255, 85}, new double[]{68, 0, 255}};

    public static void registerItemColors() {
        Minecraft.getInstance().getItemColors().register(
                (itemStack, tintIndex) -> tintIndex == 0 ? MythicItem.getColor(itemStack) : 0xFFFFFF,
                MYTHIC_HELMET.get(), MYTHIC_CHESTPLATE.get(), MYTHIC_LEGGINGS.get(), MYTHIC_BOOTS.get(), MYTHIC_SWORD.get(), MYTHIC_AXE.get(), MYTHIC_SHOVEL.get(), MYTHIC_PICKAXE.get(), MYTHIC_HOE.get(), MYTHIC_ELYTRA.get()
        );
    }
}

package net.boston.mythicarmor.item;

import net.boston.mythicarmor.MythicArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MythicArmor.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> MYTHIC_SHARD = ITEMS.register("mythic_shard", () ->
            new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGMITE_ESSENCE = ITEMS.register("magmite_essence", () ->
            new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENDERITE_ESSENCE = ITEMS.register("enderite_essence", () ->
            new Item(new Item.Properties()));
    public static final RegistryObject<Item> PROSPERITE_ESSENCE = ITEMS.register("prosperite_essence", () ->
            new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMETHITE_ESSENCE = ITEMS.register("amethite_essence", () ->
            new Item(new Item.Properties()));
    public static final RegistryObject<Item> MYTHIC_BOOTS = ITEMS.register("mythic_boots",
            () -> new ArmorItem(MythicArmorMaterial.ArmorTiers.MYTHIC, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> MYTHIC_LEGGINGS = ITEMS.register("mythic_leggings",
            () -> new ArmorItem(MythicArmorMaterial.ArmorTiers.MYTHIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MYTHIC_CHESTPLATE = ITEMS.register("mythic_chestplate",
            () -> new ArmorItem(MythicArmorMaterial.ArmorTiers.MYTHIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MYTHIC_HELMET = ITEMS.register("mythic_helmet",
            () -> new ArmorItem(MythicArmorMaterial.ArmorTiers.MYTHIC, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final Item[] ESSENCES_ARR = {MAGMITE_ESSENCE.get(), ENDERITE_ESSENCE.get(), PROSPERITE_ESSENCE.get(), AMETHITE_ESSENCE.get()};
}

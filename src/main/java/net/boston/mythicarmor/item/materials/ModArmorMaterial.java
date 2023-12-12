package net.boston.mythicarmor.item.materials;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {
    MYTHIC("mythic", 45, Util.make(new EnumMap<>(ArmorItem.Type.class), (x) -> {
        x.put(ArmorItem.Type.BOOTS, 4);
        x.put(ArmorItem.Type.LEGGINGS, 7);
        x.put(ArmorItem.Type.CHESTPLATE, 9);
        x.put(ArmorItem.Type.HELMET, 4);
    }), 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0f, 0f, () ->
        Ingredient.of(ModItems.MYTHIC_SHARD.get())
    );

    public static final StringRepresentable.EnumCodec<ArmorMaterials> CODEC = StringRepresentable.fromEnum(ArmorMaterials::values);
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModArmorMaterial(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }
    public int getDefenseForType(ArmorItem.Type type) { return this.protectionFunctionForType.get(type); }
    public int getEnchantmentValue() { return this.enchantmentValue; }
    public SoundEvent getEquipSound() { return this.sound; }
    public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    public String getName() { return MythicArmor.MOD_ID + ":" + this.name; }
    public float getToughness() { return this.toughness; }
    public float getKnockbackResistance() { return this.knockbackResistance; }
    public String GetSerializedName() { return this.name; }
}
package net.boston.mythicarmor.item;

import net.boston.mythicarmor.MythicArmor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public record MythicArmorMaterial(String name, int durability, int[] protection, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) implements ArmorMaterial {
    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return switch (pType.getSlot()) {
            case FEET -> 13;
            case LEGS -> 15;
            case CHEST -> 16;
            case HEAD -> 11;
            default -> 0;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protection[pType.getSlot().getIndex()];
    }

    public int getEnchantmentValue() {
        return 20;
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return MythicArmor.MOD_ID + ":" + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public static class ArmorTiers {
        public static final ArmorMaterial MYTHIC = new MythicArmorMaterial(
            "mythic_armor_material", 500, new int[] { 4, 7, 9, 4 }, 3f, 0.1f,
                () -> Ingredient.of(ModItems.MYTHIC_SHARD.get())
        );
    }
}

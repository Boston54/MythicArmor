package net.boston.mythicarmor.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Function;

public class MythicItemEffects {
    public static final UUID AMETHYST_HEALTH_UUID = UUID.nameUUIDFromBytes("mythicarmor:amethyst_health".getBytes());
    public static final UUID AMETHYST_SPEED_UUID = UUID.nameUUIDFromBytes("mythicarmor:amethyst_speed".getBytes());
    public static final UUID AGILITY_SPEED_UUID = UUID.nameUUIDFromBytes("mythicarmor:agility_speed".getBytes());
    public static final UUID AGILITY_ATTACKSPEED_UUID = UUID.nameUUIDFromBytes("mythicarmor:agility_attackspeed".getBytes());

    private static final HashMap<EquipmentSlot, Integer> uuidModifiers = new HashMap<>() {{
        put(EquipmentSlot.MAINHAND, 1);
        put(EquipmentSlot.OFFHAND, 2);
        put(EquipmentSlot.HEAD, 3);
        put(EquipmentSlot.CHEST, 4);
        put(EquipmentSlot.LEGS, 5);
        put(EquipmentSlot.FEET, 6);
    }};

    public enum ItemType {
        WEAPON,
        TOOL,
        ARMOR;
    }

    public enum ImbueType {
        MAGMA(0),
        ENDER(1),
        PROSPERITY(2),
        AMETHYST(3),
        AGILITY(4),
        ANCIENT(5);

        public final int typeIndex;

        ImbueType(int typeIndex) {
            this.typeIndex = typeIndex;
        }
    }

    public record Effect(UUID id, Attribute attribute, Function<Integer, Double> formula, String name, AttributeModifier.Operation operation) { }

    public static final HashMap<ItemType, HashMap<ImbueType, List<Effect>>> effects = new HashMap<>() {{
        put(ItemType.WEAPON, new HashMap<>() {{
            put(ImbueType.AGILITY, new ArrayList<>() {{
                add(new Effect(AGILITY_ATTACKSPEED_UUID, Attributes.ATTACK_SPEED, x -> (x / 100.0) * 0.5, "mythicarmor:agility_attackspeed", AttributeModifier.Operation.MULTIPLY_BASE));
            }});
        }});
        put(ItemType.TOOL, new HashMap<>() {{

        }});
        put(ItemType.ARMOR, new HashMap<>() {{
            put(ImbueType.AMETHYST, new ArrayList<>() {{
                add(new Effect(AMETHYST_HEALTH_UUID, Attributes.MAX_HEALTH, x -> x / 10.0, "mythicarmor:amethyst_health", AttributeModifier.Operation.ADDITION));
                add(new Effect(AMETHYST_SPEED_UUID, Attributes.MOVEMENT_SPEED, x -> x / -500.0, "mythicarmor:amethyst_speed", AttributeModifier.Operation.MULTIPLY_BASE));
            }});
            put(ImbueType.AGILITY, new ArrayList<>() {{
                add(new Effect(AGILITY_SPEED_UUID, Attributes.MOVEMENT_SPEED, x -> x * 0.004, "mythicarmor:agility_speed", AttributeModifier.Operation.MULTIPLY_BASE));
            }});
        }});
    }};

    public static Multimap<Attribute, AttributeModifier> updateAttributeModifiers(Multimap<Attribute, AttributeModifier> modifiers, EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> attr : modifiers.entries()) attributes.put(attr.getKey(), attr.getValue());

        if (MythicItem.isTool(stack) && effects.containsKey(ItemType.TOOL)) {
            if (slot == EquipmentSlot.MAINHAND)
                updateAttributeForItemType(attributes, stack, ItemType.TOOL, slot);
        }
        if (MythicItem.isWeapon(stack) && effects.containsKey(ItemType.WEAPON)) {
            if (slot == EquipmentSlot.MAINHAND)
                updateAttributeForItemType(attributes, stack, ItemType.WEAPON, slot);
        }
        if ((stack.getItem() instanceof MythicArmorItem || stack.getItem() instanceof MythicElytraItem) && effects.containsKey(ItemType.ARMOR)) {
            EquipmentSlot stackArmorSlot;
            if (stack.getItem() == ModItems.MYTHIC_HELMET.get()) stackArmorSlot = EquipmentSlot.HEAD;
            else if (stack.getItem() == ModItems.MYTHIC_CHESTPLATE.get() || stack.getItem() == ModItems.MYTHIC_ELYTRA.get()) stackArmorSlot = EquipmentSlot.CHEST;
            else if (stack.getItem() == ModItems.MYTHIC_LEGGINGS.get()) stackArmorSlot = EquipmentSlot.LEGS;
            else stackArmorSlot = EquipmentSlot.FEET;
            if (slot == stackArmorSlot)
                updateAttributeForItemType(attributes, stack, ItemType.ARMOR, slot);
        }

        return attributes;
    }

    private static void updateAttributeForItemType(Multimap<Attribute, AttributeModifier> modifiers, ItemStack stack, ItemType itemType, EquipmentSlot slot) {
        HashMap<ImbueType, List<Effect>> typeEffects = effects.get(itemType);
        for (ImbueType imbueType : ImbueType.values()) {
            if (typeEffects.containsKey(imbueType)) {
                int imbueAmount = MythicItem.getImbueAmount(stack, imbueType.typeIndex);
                if (imbueAmount > 0) {
                    for (Effect effect : typeEffects.get(imbueType)) {
                        changeModifier(modifiers, effect.attribute, modifyUUID(effect.id, slot), effect.formula.apply(imbueAmount), effect.name, effect.operation);
                    }
                }
            }
        }
    }

    private static void changeModifier(Multimap<Attribute, AttributeModifier> attributes, Attribute attribute, UUID uuid, double newValue, String name, AttributeModifier.Operation operation) {
        final Collection<AttributeModifier> modifiers = attributes.get(attribute);
        AttributeModifier selectedModifier = null;
        for (AttributeModifier modifier: modifiers) {
            if (modifier.getId().equals(uuid)) {
                selectedModifier = modifier;
                break;
            }
        }

        if (selectedModifier != null) {
            modifiers.remove(selectedModifier);
            modifiers.add(new AttributeModifier(selectedModifier.getId(), selectedModifier.getName(), newValue, selectedModifier.getOperation()));
        } else {
            modifiers.add(new AttributeModifier(uuid, name, newValue, operation));
        }
    }

    private static UUID modifyUUID(UUID uuid, EquipmentSlot slot) {
        int modifier = uuidModifiers.get(slot);
        return new UUID(uuid.getMostSignificantBits() + modifier, uuid.getLeastSignificantBits() + modifier);
    }
}

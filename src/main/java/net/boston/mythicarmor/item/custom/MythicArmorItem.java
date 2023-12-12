package net.boston.mythicarmor.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.boston.mythicarmor.item.custom.MythicItem.*;

public class MythicArmorItem extends ArmorItem {
    public MythicArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addImbueTooltip(item, pTooltipComponents);

        // Detailed imbued stats
        int totalImbue = getTotalImbue(item);
        if (totalImbue > 0) {
            pTooltipComponents.add(Component.literal(""));
            if (Screen.hasShiftDown()) {

                // Magma
                double magma = getImbueAmount(item, 0);
                String magmaStart = ModItems.imbueColors[0] + " * §7";
                if (magma > 0) {
                    pTooltipComponents.add(Component.literal(magmaStart+"-"+(magma/2)+"% incoming fire damage"));
                    pTooltipComponents.add(Component.literal(magmaStart+"+"+(magma/4)+"% chance to set attackers on fire"));
                    if (magma >= 100) pTooltipComponents.add(Component.literal(magmaStart+"permanent fire resistance"));
                }

                // Ender
                double ender = getImbueAmount(item, 1);
                String enderStart = ModItems.imbueColors[1] + " * §7";
                if (ender > 0) {
                    double dropChance = ender * 2 > 100 ? 100.0 : ender * 2;
                    pTooltipComponents.add(Component.literal(enderStart+(dropChance)+"% chance to keep this item on death"));
                    pTooltipComponents.add(Component.literal(enderStart+"-"+(ender/8)+"% incoming damage from enemies with"));
                    pTooltipComponents.add(Component.literal("§7   more than 100 max health"));
                }

                // Prosperity
                double prosp = getImbueAmount(item, 2);
                String prospStart = ModItems.imbueColors[2] + " * §7";
                if (prosp > 0) {
                    pTooltipComponents.add(Component.literal(prospStart+"+"+(prosp/8)+"% chance to dodge incoming damage"));
                }

                // Amethyst
                double amethyst = getImbueAmount(item, 3);
                String amethystStart = ModItems.imbueColors[3] + " * §7";
                if (amethyst > 0) {
                    pTooltipComponents.add(Component.literal(amethystStart+"+"+(amethyst/10)+" max health"));
                    pTooltipComponents.add(Component.literal(amethystStart+"-"+(amethyst/8)+"% movement speed"));
                }

                // Agility
                double agility = getImbueAmount(item, 4);
                String agilityStart = ModItems.imbueColors[4] + " * §7";
                if (agility > 0) {
                    pTooltipComponents.add(Component.literal(agilityStart+"+"+(agility*0.75)+"% movement speed"));
                }

            } else {
                pTooltipComponents.add(Component.literal("§2[SHIFT] for imbued stats"));
            }
        }

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        final Multimap<Attribute, AttributeModifier> superAttributes = super.getAttributeModifiers(slot, stack);
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> attr : superAttributes.entries()) attributes.put(attr.getKey(), attr.getValue());

        if (stack.getItem() instanceof MythicArmorItem) {
            EquipmentSlot stackArmorSlot;
            if (stack.getItem() == ModItems.MYTHIC_HELMET.get()) stackArmorSlot = EquipmentSlot.HEAD;
            else if (stack.getItem() == ModItems.MYTHIC_CHESTPLATE.get()) stackArmorSlot = EquipmentSlot.CHEST;
            else if (stack.getItem() == ModItems.MYTHIC_LEGGINGS.get()) stackArmorSlot = EquipmentSlot.LEGS;
            else stackArmorSlot = EquipmentSlot.FEET;

            if (slot == stackArmorSlot) {
                // Amethyst
                int amethyst = MythicItem.getImbueAmount(stack, 3);
                if (amethyst > 0) {
                    changeModifier(attributes, Attributes.MAX_HEALTH, MythicItem.AMETHYST_HEALTH_UUID, amethyst / 10.0, "mythicarmor:amethyst_health", AttributeModifier.Operation.ADDITION);
                    changeModifier(attributes, Attributes.MOVEMENT_SPEED, MythicItem.AMETHYST_SPEED_UUID, amethyst / -800.0, "mythicarmor:amethyst_speed", AttributeModifier.Operation.MULTIPLY_BASE);
                }

                // Agility
                int agility = MythicItem.getImbueAmount(stack, 4);
                if (agility > 0) {
                    changeModifier(attributes, Attributes.MOVEMENT_SPEED, MythicItem.AGILITY_SPEED_UUID, agility * 0.0075, "mythicarmor:agility_speed", AttributeModifier.Operation.MULTIPLY_BASE);
                }
            }
        }

        return attributes;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            List<ItemStack> equipment = List.of(helmet, chestplate, leggings, boots);
            if (equipment.stream().anyMatch(armor -> MythicItem.getImbueAmount(armor, 0) >= 100)) {
                if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) return;

                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
        }
    }
}

package net.boston.mythicarmor.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.boston.mythicarmor.item.custom.MythicItem.*;

public class MythicSwordItem extends SwordItem {
    public MythicSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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
                    pTooltipComponents.add(Component.literal(magmaStart+"+"+(magma*0.75)+"% outgoing damage to non-fire-resistant"));
                    pTooltipComponents.add(Component.literal("§7   enemies"));
                }

                // Ender
                double ender = getImbueAmount(item, 1);
                String enderStart = ModItems.imbueColors[1] + " * §7";
                if (ender > 0) {
                    pTooltipComponents.add(Component.literal(enderStart+"+"+(ender)+"% outgoing damage to enemies with more"));
                    pTooltipComponents.add(Component.literal("§7   than 100 max health"));
                }

                // Prosperity
                double prosp = getImbueAmount(item, 2);
                String prospStart = ModItems.imbueColors[2] + " * §7";
                if (prosp > 0) {
                    pTooltipComponents.add(Component.literal(prospStart+(prosp/10)+"% chance to insta-kill any enemy with"));
                    pTooltipComponents.add(Component.literal("§7   less than 100 max health"));
                    if (prosp >= 50) {
                        if (prosp <= 99) pTooltipComponents.add(Component.literal(prospStart+"+1 level of looting"));
                        else pTooltipComponents.add(Component.literal(prospStart+"+2 levels of looting"));
                    }
                }

                // Amethyst
                double amethyst = getImbueAmount(item, 3);
                String amethystStart = ModItems.imbueColors[3] + " * §7";
                if (amethyst > 0) {
                    pTooltipComponents.add(Component.literal(amethystStart+"+"+(amethyst/2)+"% outgoing damage"));
                }

                // Agility
                double agility = getImbueAmount(item, 4);
                String agilityStart = ModItems.imbueColors[4] + " * §7";
                if (agility > 0) {
                    pTooltipComponents.add(Component.literal(agilityStart+"+"+(agility*0.5)+"% attack speed"));
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

        if (slot == EquipmentSlot.MAINHAND && stack.getItem() instanceof MythicSwordItem) {
            int agility = MythicItem.getImbueAmount(stack, 4);
            if (agility > 0) {
                changeModifier(attributes, Attributes.ATTACK_SPEED, MythicItem.AGILITY_ATTACKSPEED_UUID, (agility / 100.0) * 0.5, "mythicarmor:agility_attackspeed", AttributeModifier.Operation.MULTIPLY_BASE);
            }
        }
        return attributes;
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchants = super.getAllEnchantments(stack);

        // Prosperity
        int prosperity = MythicItem.getImbueAmount(stack, 2);
        if (prosperity >= 50) {
            changeEnchant(enchants, Enchantments.MOB_LOOTING, (int)Math.floor(prosperity / 50.0));
        }

        return enchants;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        // Prosperity
        if (enchantment.equals(Enchantments.MOB_LOOTING)) {
            int prosperity = MythicItem.getImbueAmount(stack, 2);
            level += (int)Math.floor(prosperity / 50.0);
        }

        return level;
    }
}

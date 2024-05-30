package net.boston.mythicarmor.item.custom;

import com.google.common.collect.Multimap;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static net.boston.mythicarmor.item.custom.MythicItem.*;

public class MythicElytraItem extends ElytraItem implements ColorableMythicItem {
    public MythicElytraItem(Properties pProperties) {
        super(pProperties);
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
                    pTooltipComponents.add(Component.literal(amethystStart+"-"+(amethyst/5)+"% movement speed"));
                    if (amethyst == 100)
                        pTooltipComponents.add(Component.literal(amethystStart+"Unbreakable"));
                }

                // Agility
                double agility = getImbueAmount(item, 4);
                String agilityStart = ModItems.imbueColors[4] + " * §7";
                if (agility > 0) {
                    pTooltipComponents.add(Component.literal(agilityStart+"+"+(agility*0.4)+"% movement speed"));
                }

            } else {
                pTooltipComponents.add(Component.literal("§2[SHIFT] for imbued stats"));
            }
            pTooltipComponents.add(Component.literal(""));
        }

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        final Multimap<Attribute, AttributeModifier> superAttributes = super.getAttributeModifiers(slot, stack);

        if (stack.getItem() instanceof MythicArmorItem || stack.getItem() instanceof MythicElytraItem)
            return MythicItemEffects.updateAttributeModifiers(superAttributes, slot, stack);
        return superAttributes;
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

    public static boolean isUseable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }
}

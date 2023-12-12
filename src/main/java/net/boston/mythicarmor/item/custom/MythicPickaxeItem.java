package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.boston.mythicarmor.item.custom.MythicItem.*;

public class MythicPickaxeItem extends PickaxeItem {
    public MythicPickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
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
                // Prosperity
                double prosp = getImbueAmount(item, 2);
                String prospStart = ModItems.imbueColors[2] + " * §7";
                if (prosp >= 50) {
                    if (prosp <= 99) pTooltipComponents.add(Component.literal(prospStart+"+1 level of fortune"));
                    else pTooltipComponents.add(Component.literal(prospStart+"+2 levels of fortune"));
                }

                // Amethyst
                double amethyst = getImbueAmount(item, 3);
                String amethystStart = ModItems.imbueColors[3] + " * §7";
                if (amethyst > 0) {
                    pTooltipComponents.add(Component.literal(amethystStart+(amethyst/4)+"% chance to repair by 1 durability when"));
                    pTooltipComponents.add(Component.literal("§7   breaking a block"));
                    if (amethyst >= 100) pTooltipComponents.add(Component.literal(amethystStart+"unbreakable"));
                }

                // Agility
                double agility = getImbueAmount(item, 4);
                String agilityStart = ModItems.imbueColors[4] + " * §7";
                if (agility > 0) {
                    if (agility >= 50) pTooltipComponents.add(Component.literal(agilityStart+"+"+((int)Math.floor(agility/25))+" levels of efficiency"));
                    else if (agility >= 25) pTooltipComponents.add(Component.literal(agilityStart+"+"+((int)Math.floor(agility/25))+" level of efficiency"));
                }

            } else {
                pTooltipComponents.add(Component.literal("§2[SHIFT] for imbued stats"));
            }
        }

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!(itemStack.getItem() instanceof MythicPickaxeItem)) return;

        MythicItem.toolOnBlockBreak(event, itemStack, player);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchants = super.getAllEnchantments(stack);

        // Prosperity
        int prosperity = MythicItem.getImbueAmount(stack, 2);
        if (prosperity >= 50) {
            changeEnchant(enchants, Enchantments.BLOCK_FORTUNE, (int)Math.floor(prosperity / 50.0));
        }

        // Agility
        int agility = MythicItem.getImbueAmount(stack, 4);
        if (agility >= 25) {
            changeEnchant(enchants, Enchantments.BLOCK_EFFICIENCY, (int)Math.floor(agility / 25.0));
        }

        return enchants;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        // Prosperity
        if (enchantment.equals(Enchantments.BLOCK_FORTUNE)) {
            int prosperity = MythicItem.getImbueAmount(stack, 2);
            level += (int)Math.floor(prosperity / 50.0);
        }

        // Agility
        else if (enchantment.equals(Enchantments.BLOCK_EFFICIENCY)) {
            int agility = MythicItem.getImbueAmount(stack, 4);
            level += (int)Math.floor(agility / 25.0);
        }

        return level;
    }
}

package net.boston.mythicarmor.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
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

import static net.boston.mythicarmor.item.custom.MythicItem.addImbueTooltip;
import static net.boston.mythicarmor.item.custom.MythicItem.changeEnchant;

public class MythicHoeItem extends HoeItem implements ColorableMythicItem {
    public MythicHoeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addImbueTooltip(item, pTooltipComponents);

        MythicItem.createDetailedImbueDetails(pTooltipComponents, item);

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = (player.getItemBySlot(EquipmentSlot.MAINHAND));
        if (!(itemStack.getItem() instanceof MythicHoeItem)) return;

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

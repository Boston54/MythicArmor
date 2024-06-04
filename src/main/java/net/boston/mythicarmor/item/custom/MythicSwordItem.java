package net.boston.mythicarmor.item.custom;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

import static net.boston.mythicarmor.item.custom.MythicItem.addImbueTooltip;
import static net.boston.mythicarmor.item.custom.MythicItem.changeEnchant;

public class MythicSwordItem extends SwordItem implements ColorableMythicItem {
    public MythicSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addImbueTooltip(item, pTooltipComponents);

        MythicItem.createDetailedImbueDetails(pTooltipComponents, item);

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> initialModifiers = super.getAttributeModifiers(slot, stack);
        return MythicItemEffects.updateAttributeModifiers(initialModifiers, slot, stack);
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

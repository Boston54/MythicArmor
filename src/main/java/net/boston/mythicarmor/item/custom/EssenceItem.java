package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EssenceItem extends Item {

    public EssenceItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int i = essenceItemToIndex((EssenceItem)item.getItem());
        switch (i) {
            case 0:
                // Magma
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Armor:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 -0.5% incoming fire damage"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 100% on one piece) permanent fire resistance"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.25% chance to set attackers on fire"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Weapons:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.75% outgoing damage to non-fireproof enemies"));
                break;
            case 1:
                // Ender
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Armor:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 -0.125% incoming damage from enemies with"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "§7   more than 100 max health"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Weapons:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +1% outgoing damage to enemies with more"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "§7   than 100 max health"));
                break;
            case 2:
                // Prosperity
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Armor:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.125% chance to dodge incoming damage"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Weapons:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.1% chance to instantly kill any enemy with"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "§7   less than 100 max health"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 50%) +1 looting level"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 100%) +1 looting level (+2 total)"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Tools:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 50%) +1 fortune level"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 100%) +1 fortune level (+2 total)"));
                break;
            case 3:
                // Amethyst
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Armor:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.1 max health"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 -0.125% movement speed"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Weapons:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.5% outgoing damage"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Tools:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.25% chance to repair 1 durability when"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "§7   breaking a block"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To All:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 100%) unbreakable"));
                break;
            case 4:
                // Agility
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Armor:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.75% movement speed"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Weapons:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 +0.5% attack speed"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + "To Tools:"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 25%) +1 efficiency level"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 50%) +1 efficiency level (+2 total)"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 75%) +1 efficiency level (+3 total)"));
                pTooltipComponents.add(Component.literal(ModItems.imbueColors[i] + " *§7 (at 100%) +1 efficiency level (+4 total)"));
        }
        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private int essenceItemToIndex(EssenceItem essenceItem) {
        if (essenceItem == ModItems.MAGMA_ESSENCE.get()) return 0;
        if (essenceItem == ModItems.ENDER_ESSENCE.get()) return 1;
        if (essenceItem == ModItems.PROSPERITY_ESSENCE.get()) return 2;
        if (essenceItem == ModItems.AMETHYST_ESSENCE.get()) return 3;
        if (essenceItem == ModItems.AGILITY_ESSENCE.get()) return 4;
        return 0;
    }
}

package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.util.ModStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Random;

import static net.boston.mythicarmor.item.ModItems.tagNames;

public interface ColorableMythicItem extends DyeableLeatherItem {
    @Override
    default boolean hasCustomColor(ItemStack stack) {
        return MythicItem.getTotalImbue(stack) > 0;
    }

    @Override
    default int getColor(ItemStack stack) {
        return MythicItem.getColor(stack);
    }

    @Override
    default void clearColor(ItemStack stack) {
//        CompoundTag displayTag = stack.getTagElement("display");
//        if (displayTag != null && displayTag.contains("color")) {
//            displayTag.remove("color");
//        }
    }

    default void burnImbuements(ItemStack stack, ServerPlayer player) {
        // Get the imbuements on the item for each type
        HashMap<MythicItemEffects.ImbueType, Integer> currentImbuements = new HashMap<>();
        for (MythicItemEffects.ImbueType type : MythicItemEffects.ImbueType.values()) {
            int amount = MythicItem.getImbueAmount(stack, type.typeIndex);
            if (amount > 0)
                currentImbuements.put(type, amount);
        }

        // Burn up to 10 imbuements off, but limit it at the total imbue amount
        int burnAmount = Math.min(currentImbuements.values().stream().mapToInt(Integer::intValue).sum(), 10);

        Random random = new Random();
        // Remove burnAmount imbuements
        for (int i = 0; i < burnAmount; i++) {
            // Choose a random imbuement that this item has and subtract 1 to account for its removal
            MythicItemEffects.ImbueType[] choices = currentImbuements.keySet().toArray(MythicItemEffects.ImbueType[]::new);
            MythicItemEffects.ImbueType type = choices[random.nextInt(choices.length)];
            currentImbuements.put(type, currentImbuements.get(type) - 1);
            if (currentImbuements.get(type) == 0) currentImbuements.remove(type);

            String key = tagNames[type.typeIndex];
            // Get the tag, or create one if it has none
            CompoundTag nbtTag = stack.getTag();
            // Get the int
            int imbueAmount = nbtTag.getInt(key);
            // Modify the int
            nbtTag.putInt(key, imbueAmount - 1);
        }

        player.awardStat(ModStats.BURNT_IMBUEMENTS_STAT, 10);
        System.out.println(player.getStats().getValue(ModStats.BURNT_IMBUEMENTS_STAT));
    }

    @Override
    default void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement("display").putInt("color", color);
    }
}

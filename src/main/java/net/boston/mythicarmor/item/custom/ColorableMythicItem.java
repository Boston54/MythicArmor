package net.boston.mythicarmor.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

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
        CompoundTag displayTag = stack.getTagElement("display");
        if (displayTag != null && displayTag.contains("color")) {
            displayTag.remove("color");
        }
    }

    @Override
    default void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement("display").putInt("color", color);
    }
}

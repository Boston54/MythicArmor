package net.boston.mythicarmor.item.materials;

import net.boston.mythicarmor.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolMaterial {
    public static final ForgeTier MYTHIC = new ForgeTier(4, 2500, 10.0f, 0f, 18,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ModItems.MYTHIC_SHARD.get()));
}

package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.item.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.Random;

public class MythicLavaCauldronInteraction {
    static CauldronInteraction MYTHIC_ITEM_INTERACTION = (blockState, level, blockPos, player, interactionHand, stack) -> {
        Item item = stack.getItem();
        if (!(item instanceof ColorableMythicItem mythicItem)) {
            return InteractionResult.PASS;
        } else if (!mythicItem.hasCustomColor(stack)) {
            return InteractionResult.PASS;
        } else {
            if (!level.isClientSide) {
                mythicItem.burnImbuements(stack, (ServerPlayer)player);

                // 10% Chance to remove the lava
                if ((new Random()).nextInt(10) == 0) {
                    level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                    level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f);
                } else {
                    level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 2f);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    };

    public static void init() {
        for (RegistryObject<Item> mythicItem : ModItems.MYTHIC_ITEMS_ARR)
            CauldronInteraction.LAVA.put(mythicItem.get(), MYTHIC_ITEM_INTERACTION);
    }
}

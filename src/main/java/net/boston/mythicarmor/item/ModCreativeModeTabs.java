package net.boston.mythicarmor.item;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MythicArmor.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MYTHIC_ARMOR_TAB = CREATIVE_MODE_TABS.register("mythic_armor_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MYTHIC_SHARD.get()))
                    .title(Component.translatable("creativetab.mythic_armor_tab"))
                    .displayItems((nParameters, pOutput) -> {
                        pOutput.accept(ModItems.MYTHIC_SHARD.get());
                        pOutput.accept(ModItems.MYTHIC_INGOT.get());
                        pOutput.accept(ModBlocks.MYTHIC_ORE.get());
                        pOutput.accept(ModItems.MAGMA_ESSENCE.get());
                        pOutput.accept(ModItems.ENDER_ESSENCE.get());
                        pOutput.accept(ModItems.PROSPERITY_ESSENCE.get());
                        pOutput.accept(ModItems.AMETHYST_ESSENCE.get());
                        pOutput.accept(ModItems.AGILITY_ESSENCE.get());
                        pOutput.accept(ModItems.MYTHIC_HELMET.get());
                        pOutput.accept(ModItems.MYTHIC_CHESTPLATE.get());
                        pOutput.accept(ModItems.MYTHIC_LEGGINGS.get());
                        pOutput.accept(ModItems.MYTHIC_BOOTS.get());
                        pOutput.accept(ModItems.MYTHIC_SWORD.get());
                        pOutput.accept(ModItems.MYTHIC_AXE.get());
                        pOutput.accept(ModItems.MYTHIC_PICKAXE.get());
                        pOutput.accept(ModItems.MYTHIC_SHOVEL.get());
                        pOutput.accept(ModBlocks.IMBUING_STATION.get());
                        pOutput.accept(ModItems.MYTHIC_UPGRADE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

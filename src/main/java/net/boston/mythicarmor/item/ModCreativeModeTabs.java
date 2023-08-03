package net.boston.mythicarmor.item;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.block.custom.ModBlocks;
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
                        pOutput.accept(ModBlocks.MYTHIC_ORE.get());
                        pOutput.accept(ModItems.ENDERITE_ESSENCE.get());
                        pOutput.accept(ModItems.MAGMITE_ESSENCE.get());
                        pOutput.accept(ModItems.PROSPERITE_ESSENCE.get());
                        pOutput.accept(ModItems.AMETHITE_ESSENCE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

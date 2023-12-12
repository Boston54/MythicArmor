package net.boston.mythicarmor;

import com.mojang.logging.LogUtils;
import net.boston.mythicarmor.block.ModBlocks;
import net.boston.mythicarmor.block.entity.ModBlockEntities;
import net.boston.mythicarmor.gui.ImbuingStationScreen;
import net.boston.mythicarmor.gui.ModMenuTypes;
import net.boston.mythicarmor.item.ModCreativeModeTabs;
import net.boston.mythicarmor.item.ModItems;
import net.boston.mythicarmor.item.custom.MythicAxeItem;
import net.boston.mythicarmor.item.custom.MythicPickaxeItem;
import net.boston.mythicarmor.item.custom.MythicShovelItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MythicArmor.MOD_ID)
public class MythicArmor {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mythicarmor";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MythicArmor() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(MythicPickaxeItem.class);
        MinecraftForge.EVENT_BUS.register(MythicAxeItem.class);
        MinecraftForge.EVENT_BUS.register(MythicShovelItem.class);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.IMBUING_STATION_MENU.get(), ImbuingStationScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.IMBUING_STATION.get(), RenderType.solid());
        }
    }
}

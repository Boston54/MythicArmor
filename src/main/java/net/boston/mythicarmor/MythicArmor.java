package net.boston.mythicarmor;

import com.mojang.logging.LogUtils;
import net.boston.mythicarmor.block.ModBlocks;
import net.boston.mythicarmor.block.entity.ModBlockEntities;
import net.boston.mythicarmor.gui.ImbuingStationScreen;
import net.boston.mythicarmor.gui.ModMenuTypes;
import net.boston.mythicarmor.item.ModCreativeModeTabs;
import net.boston.mythicarmor.item.ModItems;
import net.boston.mythicarmor.item.custom.*;
import net.boston.mythicarmor.item.elytra.MythicElytraArmorStandLayer;
import net.boston.mythicarmor.item.elytra.MythicElytraLayer;
import net.boston.mythicarmor.util.ModStats;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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
        MinecraftForge.EVENT_BUS.register(MythicHoeItem.class);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        if(FMLEnvironment.dist.isClient()) modEventBus.addListener(this::registerElytraLayer);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MythicLavaCauldronInteraction.init();
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
            ModItems.registerItemColors();
            ModStats.init();

            // broken elytra property
            ItemProperties.register(ModItems.MYTHIC_ELYTRA.get(), new ResourceLocation(MOD_ID, "broken"), (stack, a1, a2, a3) -> MythicElytraItem.isUseable(stack) ? 0 : 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerElytraLayer(EntityRenderersEvent event) {
        if (event instanceof EntityRenderersEvent.AddLayers addLayersEvent) {
            EntityModelSet entityModels = addLayersEvent.getEntityModels();
            addLayersEvent.getSkins().forEach(s -> {
                LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = addLayersEvent.getSkin(s);
                if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new MythicElytraLayer(playerRenderer, entityModels));
                }
            });
            LivingEntityRenderer<ArmorStand, ? extends EntityModel<ArmorStand>> livingEntityRenderer = addLayersEvent.getRenderer(EntityType.ARMOR_STAND);
            if (livingEntityRenderer instanceof ArmorStandRenderer armorStandRenderer) {
                armorStandRenderer.addLayer(new MythicElytraArmorStandLayer(armorStandRenderer, entityModels));
            }
        }
    }
}

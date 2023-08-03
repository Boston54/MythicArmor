package net.boston.mythicarmor.block.entity;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.block.custom.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MythicArmor.MOD_ID);

    public static final RegistryObject<BlockEntityType<ImbuingStationBlockEntity>> IMBUING_STATION =
            BLOCK_ENTITIES.register("imbuing_station", () ->
                    BlockEntityType.Builder.of(ImbuingStationBlockEntity::new,
                            ModBlocks.IMBUING_STATION.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

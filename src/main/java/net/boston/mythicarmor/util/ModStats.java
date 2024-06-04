package net.boston.mythicarmor.util;

import net.boston.mythicarmor.MythicArmor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;

public class ModStats {
    public static ResourceLocation BURNT_IMBUEMENTS = new ResourceLocation(MythicArmor.MOD_ID, "burnt_imbuements");
    public static Stat<ResourceLocation> BURNT_IMBUEMENTS_STAT;

    public static void init() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, ModStats.BURNT_IMBUEMENTS, ModStats.BURNT_IMBUEMENTS);
        BURNT_IMBUEMENTS_STAT = Stats.CUSTOM.get(BURNT_IMBUEMENTS);
    }
}
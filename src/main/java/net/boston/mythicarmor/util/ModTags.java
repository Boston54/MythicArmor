package net.boston.mythicarmor.util;

import net.boston.mythicarmor.MythicArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> MYTHIC = tag("mythic");
        public static final TagKey<Item> ESSENCE = tag("essence");

        public static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MythicArmor.MOD_ID, name));
        }
    }
}

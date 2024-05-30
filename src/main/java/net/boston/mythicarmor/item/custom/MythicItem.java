package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

import static net.boston.mythicarmor.item.ModItems.*;

@Mod.EventBusSubscriber(modid = "mythicarmor", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MythicItem {
    public static final int maxTotalImbue = 100;
    private static double[] DEFAULT_COLOR = {169, 0, 135};

    public static int getTotalImbue(ItemStack item) {
        int x = 0;
        for (int i = 0; i < imbueNames.length; i++) {
            x += getImbueAmount(item, i);
        }
        return x;
    }

    public static void imbue(ItemStack item, EssenceItem essenceItem) {
        String key = essenceToTagName(essenceItem);
        // Get the tag, or create one if it has none
        CompoundTag nbtTag = item.getTag();
        if (nbtTag == null) nbtTag = new CompoundTag();
        // Get the int
        int imbueAmount = nbtTag.getInt(key);
        // Modify the int
        nbtTag.putInt(key, imbueAmount + 1);

        // Add Unbreakable modifier at 100% amethyst
        int amethyst = MythicItem.getImbueAmount(item, 3);
        if (amethyst == 100) {
            // Set unbreakable
            nbtTag.putBoolean("Unbreakable", true);
        }

        // Give the tag back to the item
        item.setTag(nbtTag);
    }

    public static int getImbueAmount(ItemStack item, int typeIndex) {
        if (item == null) return 0;
        CompoundTag tag = item.getTag();
        if (tag == null) return 0;
        return tag.getInt(indexToTagName(typeIndex));
    }

    public static String indexToTagName(int index) {
        return tagNames[index];
    }

    public static String essenceToTagName(EssenceItem essenceItem){
        if (essenceItem == ModItems.MAGMA_ESSENCE.get()) return tagNames[0];
        if (essenceItem == ModItems.ENDER_ESSENCE.get()) return tagNames[1];
        if (essenceItem == ModItems.PROSPERITY_ESSENCE.get()) return tagNames[2];
        if (essenceItem == ModItems.AMETHYST_ESSENCE.get()) return tagNames[3];
        if (essenceItem == ModItems.AGILITY_ESSENCE.get()) return tagNames[4];
        return tagNames[0];
    }

    public static boolean canImbueAmount(ItemStack item, int amount) {
        return getTotalImbue(item) + amount <= maxTotalImbue;
    }

    public static boolean isValidImbue(ItemStack essence, ItemStack mythicItem) {
        // Any incompatible imbues should go here

        // Magma cannot be applied to tools
        if (essence.getItem() == ModItems.MAGMA_ESSENCE.get() && isTool(mythicItem) && !isWeapon(mythicItem)) return false;
        // Ender cannot be applied to tools
        if (essence.getItem() == ModItems.ENDER_ESSENCE.get() && isTool(mythicItem) && !isWeapon(mythicItem)) return false;

        // All tests passed, by default return true
        return true;
    }

    public static boolean isTool(ItemStack mythicItem) {
        Item item = mythicItem.getItem();
        return item == ModItems.MYTHIC_AXE.get() || item == ModItems.MYTHIC_PICKAXE.get() || item == ModItems.MYTHIC_SHOVEL.get();
    }

    public static boolean isWeapon(ItemStack mythicItem) {
        Item item = mythicItem.getItem();
        return item == ModItems.MYTHIC_AXE.get() || item == ModItems.MYTHIC_SWORD.get();
    }

    public static void addImbueTooltip(ItemStack item, List<Component> pTooltipComponents) {
        int totalImbue = getTotalImbue(item);

        // Imbuement percentage and tutorial
        if (totalImbue == 100)
            pTooltipComponents.add(Component.literal("§dThis item is fully imbued."));
        else {
            pTooltipComponents.add(Component.literal("This item is §e" + getTotalImbue(item) + "%§f imbued."));
            if (totalImbue == 0)
                pTooltipComponents.add(Component.literal("Use §dEssence §fat an §dImbuing Station §fto imbue."));
        }
        pTooltipComponents.add(Component.literal(""));

        // Current imbue percentages
        for (int i = 0; i < imbueNames.length; i++) {
            int imbueAmount = getImbueAmount(item, i);
            if (imbueAmount > 0)
                pTooltipComponents.add(Component.literal(imbueColors[i] + imbueNames[i] + "§f: " + imbueAmount + "%"));
        }
    }

    private static double getProc() {
        return Math.random() * 100;
    }

    public static void toolOnBlockBreak(BlockEvent.BreakEvent event, ItemStack itemStack, Player player) {

        // Amethyst
        float amethyst = getImbueAmount(itemStack, 3);
        if (amethyst > 0) {
            if (itemStack.getDamageValue() < itemStack.getMaxDamage() && getProc() < (amethyst / 4)) {
                itemStack.setDamageValue(itemStack.getDamageValue()-1);
            }
        }
    }

    public static void changeEnchant(Map<Enchantment, Integer> enchantments, Enchantment enchantToChange, int addAmount) {
        if (enchantments.containsKey(enchantToChange)) {
            enchantments.put(enchantToChange, enchantments.get(enchantToChange) + addAmount);
        } else {
            enchantments.put(enchantToChange, addAmount);
        }
    }

    @SubscribeEvent
    public static void onEntityTakeDamage(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        float damageAmount = event.getAmount();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        // Get the equipment from the attacker and target

        ItemStack targetHelmet = target.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack targetChestplate = target.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack targetLeggings = target.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack targetBoots = target.getItemBySlot(EquipmentSlot.FEET);
        List<ItemStack> targetEquipment = List.of(targetHelmet, targetChestplate, targetLeggings, targetBoots);


        List<ItemStack> attackerEquipment = null;
        ItemStack attackerWeapon = null;

        boolean attackerLiving = false;
        LivingEntity livingAttacker = null;

        if (attacker instanceof LivingEntity living) {
            attackerLiving = true;
            livingAttacker = living;

            ItemStack attackerHelmet = living.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack attackerChestplate = living.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack attackerLeggings = living.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack attackerBoots = living.getItemBySlot(EquipmentSlot.FEET);
            attackerEquipment = List.of(attackerHelmet, attackerChestplate, attackerLeggings, attackerBoots);

            attackerWeapon = livingAttacker.getItemBySlot(EquipmentSlot.MAINHAND);
        }

        // do prosperity first so the dodging can take effect first
        // Prosperity
        int targetProsperity = getImbueFromEquipment(targetEquipment, 2);
        int attackerProsperity = getImbueAmount(attackerWeapon, 2);
        if (targetProsperity > 0) {
            // +0.125% chance to dodge an attack
            if (getProc() < targetProsperity * 0.125) {
                // Dodge attack
                event.setCanceled(true);
                return;
            }
        }
        if (attackerProsperity > 0) {
            // +0.1% chance to instantly kill any enemy with less than 100 max health
            if (attackerLiving && livingAttacker.getMaxHealth() < 100) {
                if (getProc() < attackerProsperity * 0.1) {
                    event.setAmount(10000);
                }
            }
        }

        // Magma
        int targetMagma = getImbueFromEquipment(targetEquipment, 0);
        int attackerMagma = getImbueAmount(attackerWeapon, 0);
        if (attacker != null) {
            // Set attacker on fire for 4 seconds
            if (!attacker.fireImmune() && getProc() < targetMagma / 4.0) {
                attacker.setRemainingFireTicks(80);
            }
            // +0.75% outgoing damage to non-fireproof enemies
            if (!target.fireImmune()) {
                damageAmount += damageAmount * ((attackerMagma * 0.75f) / 100);
            }
        } else {
            // Reduce damage by 0.5% per level if fire
            List<ResourceKey<DamageType>> fireSources = List.of(DamageTypes.IN_FIRE, DamageTypes.ON_FIRE, DamageTypes.LAVA);
            if (fireSources.stream().anyMatch(damageSource::is)) {
                damageAmount -= damageAmount * ((targetMagma * 0.5f) / 100);
            }
        }

        // Ender
        int targetEnder = getImbueFromEquipment(targetEquipment, 1);
        int attackerEnder = getImbueAmount(attackerWeapon, 1);
        if (attackerLiving) {
            // +1% outgoing damage to enemies with more than 100 max hp
            if (attackerEnder > 0 && target.getMaxHealth() >= 100) {
                // Increase the damage
                damageAmount += damageAmount * (targetEnder / 100.0);
            }
            // -0.125% incoming damage from enemies with more than 100 max hp
            if (targetEnder > 0 && livingAttacker.getMaxHealth() >= 100) {
                // Reduce the damage
                damageAmount -= damageAmount * ((targetEnder * 0.125) / 100);
            }
        }

        // Amethyst
        int attackerAmethyst = getImbueFromEquipment(targetEquipment, 3);
        if (attackerAmethyst > 0) {
            // +0.5% outgoing damage
            damageAmount += damageAmount * ((attackerAmethyst * 0.5) / 100);
        }

        // Agility
        // no damage effects

        // Save the new information about this event
        event.setAmount(damageAmount);

    }

    public static int getImbueFromEquipment(List<ItemStack> equipment, int imbueId) {
        int total = 0;
        for (ItemStack item : equipment) total += getImbueAmount(item, imbueId);
        return total;
    }

    public static int getColor(ItemStack stack) {
        int numImbues = ModItems.imbueNames.length;
        double totalImbue = (100 - MythicItem.getTotalImbue(stack)) / 100.0;
        double[] rgb = {DEFAULT_COLOR[0] * totalImbue, DEFAULT_COLOR[1] * totalImbue, DEFAULT_COLOR[2] * totalImbue};
        for (int imbueType = 0; imbueType < numImbues; imbueType++) {
            double weighting = MythicItem.getImbueAmount(stack, imbueType) / 100.0;
            double[] color = ModItems.imbueRGBs[imbueType];
            for (int i = 0; i < 3; i++)
                rgb[i] += color[i] * weighting;
        }
        return (int) ((Math.floor(rgb[0]) * 65536) + (Math.floor(rgb[1]) * 256) + Math.floor(rgb[2]));
    }
}

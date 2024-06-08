package net.boston.mythicarmor.item.custom;

import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

import static net.boston.mythicarmor.item.ModItems.*;

@Mod.EventBusSubscriber(modid = MythicArmor.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MythicItem {
    public static final int maxTotalImbue = 100;
    private static double[] DEFAULT_COLOR = {169, 0, 135};

    private static List<ResourceKey<DamageType>> FIRE_SOURCES = List.of(DamageTypes.IN_FIRE, DamageTypes.ON_FIRE, DamageTypes.LAVA);

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
        if (essenceItem == ModItems.ANCIENT_ESSENCE.get()) return tagNames[5];
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
        // Ancient cannot be applied to tools
        if (essence.getItem() == ModItems.ANCIENT_ESSENCE.get() && isTool(mythicItem) && !isWeapon(mythicItem)) return false;

        // All tests passed, by default return true
        return true;
    }

    public static boolean isTool(ItemStack mythicItem) {
        if (mythicItem == null) return false;
        Item item = mythicItem.getItem();
        return item == ModItems.MYTHIC_AXE.get() || item == ModItems.MYTHIC_PICKAXE.get() || item == ModItems.MYTHIC_SHOVEL.get() || item == ModItems.MYTHIC_HOE.get();
    }

    public static boolean isWeapon(ItemStack mythicItem) {
        if (mythicItem == null) return false;
        Item item = mythicItem.getItem();
        return item == ModItems.MYTHIC_AXE.get() || item == ModItems.MYTHIC_SWORD.get();
    }

    public static boolean isArmor(ItemStack mythicItem) {
        if (mythicItem == null) return false;
        Item item = mythicItem.getItem();
        return item instanceof MythicArmorItem || item == MYTHIC_ELYTRA.get();
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

        System.out.println("LivingHurtEvent");


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
        boolean attackerIsWeapon = isWeapon(attackerWeapon);

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
        if (attackerProsperity > 0 && attackerIsWeapon) {
            // +0.1% chance to instantly kill any enemy with less than 100 max health
            if (livingAttacker.getMaxHealth() < 100) {
                if (getProc() < attackerProsperity * 0.1) {
                    if (attacker instanceof Player player)
                        target.hurt(target.level().damageSources().playerAttack(player), 10000);
                    else
                        target.hurt(target.level().damageSources().mobAttack((LivingEntity) attacker), 10000);
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
            if (!target.fireImmune() && isWeapon(attackerWeapon)) {
                damageAmount += damageAmount * ((attackerMagma * 0.75f) / 100);
            }
        } else {
            // Reduce damage by 0.5% per level if fire
            if (FIRE_SOURCES.stream().anyMatch(damageSource::is)) {
                damageAmount -= damageAmount * ((targetMagma * 0.5f) / 100);
            }
        }

        // Ender
        int targetEnder = getImbueFromEquipment(targetEquipment, 1);
        int attackerEnder = getImbueAmount(attackerWeapon, 1);
        if (attackerLiving && isWeapon(attackerWeapon)) {
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
        if (attackerAmethyst > 0 && isWeapon(attackerWeapon)) {
            // +0.5% outgoing damage
            damageAmount += damageAmount * ((attackerAmethyst * 0.5) / 100);
        }

        // Agility
        // no damage effects

        // Ancient
        // no damage effects

        // Save the new information about this event
        event.setAmount(damageAmount);
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();

        System.out.println("XP Change Event");

        ItemStack attackerHelmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack attackerChestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack attackerLeggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack attackerBoots = player.getItemBySlot(EquipmentSlot.FEET);
        List<ItemStack> attackerEquipment = List.of(attackerHelmet, attackerChestplate, attackerLeggings, attackerBoots);

        ItemStack attackerWeapon = player.getItemBySlot(EquipmentSlot.MAINHAND);

        // Ancient
        int attackerAncientEquipment = getImbueFromEquipment(attackerEquipment, 5);
        int attackerAncientWeapon = getImbueAmount(attackerWeapon, 5);
        if (attackerAncientEquipment + attackerAncientWeapon > 0) {
            float expMultiplier = 1 + (attackerAncientEquipment * 0.005f) + (attackerAncientWeapon * 0.01f);
            int xpAmount = event.getAmount();
            int modifiedXp = Math.round(xpAmount * expMultiplier);
            event.setAmount(modifiedXp);
        }
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

    public static void createDetailedImbueDetails(List<Component> tooltip, ItemStack item) {
        int totalImbue = getTotalImbue(item);
        if (totalImbue > 0) {
            tooltip.add(Component.literal(""));
            if (Screen.hasShiftDown()) {
                boolean weapon = isWeapon(item);
                boolean tool = isTool(item);
                boolean armor = isArmor(item);

                // Magma
                double magma = getImbueAmount(item, 0);
                String magmaStart = ModItems.imbueColors[0] + " * §7";
                if (magma > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(magmaStart+"+"+(magma*0.75)+"% outgoing damage to non-fire-resistant"));
                        tooltip.add(Component.literal("§7   enemies"));
                    }
                    if (tool) {

                    }
                    if (armor) {
                        tooltip.add(Component.literal(magmaStart+"-"+(magma/2)+"% incoming fire damage"));
                        tooltip.add(Component.literal(magmaStart+"+"+(magma/4)+"% chance to set attackers on fire"));
                        if (magma >= 100) tooltip.add(Component.literal(magmaStart+"permanent fire resistance"));
                    }
                }

                // Ender
                double ender = getImbueAmount(item, 1);
                String enderStart = ModItems.imbueColors[1] + " * §7";
                if (ender > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(enderStart+"+"+(ender)+"% outgoing damage to enemies with more"));
                        tooltip.add(Component.literal("§7   than 100 max health"));
                    }
                    if (tool) {

                    }
                    if (armor) {
                        tooltip.add(Component.literal(enderStart + "-" + (ender / 8) + "% incoming damage from enemies with"));
                        tooltip.add(Component.literal("§7   more than 100 max health"));
                    }
                }

                // Prosperity
                double prosp = getImbueAmount(item, 2);
                String prospStart = ModItems.imbueColors[2] + " * §7";
                if (prosp > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(prospStart+(prosp/10)+"% chance to insta-kill any enemy with"));
                        tooltip.add(Component.literal("§7   less than 100 max health"));
                        if (prosp >= 50) {
                            if (prosp <= 99) tooltip.add(Component.literal(prospStart+"+1 level of looting"));
                            else tooltip.add(Component.literal(prospStart+"+2 levels of looting"));
                        }
                    }
                    if (tool) {
                        if (prosp >= 50) {
                            if (prosp <= 99) tooltip.add(Component.literal(prospStart+"+1 level of fortune"));
                            else tooltip.add(Component.literal(prospStart+"+2 levels of fortune"));
                        }
                    }
                    if (armor) {
                        tooltip.add(Component.literal(prospStart + "+" + (prosp / 8) + "% chance to dodge incoming damage"));
                    }
                }

                // Amethyst
                double amethyst = getImbueAmount(item, 3);
                String amethystStart = ModItems.imbueColors[3] + " * §7";
                if (amethyst > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(amethystStart+"+"+(amethyst/2)+"% outgoing damage"));
                    }
                    if (tool) {
                        tooltip.add(Component.literal(amethystStart+(amethyst/4)+"% chance to repair by 1 durability when"));
                        tooltip.add(Component.literal("§7   breaking a block"));
                    }
                    if (armor) {
                        tooltip.add(Component.literal(amethystStart + "+" + (amethyst / 10) + " max health"));
                        tooltip.add(Component.literal(amethystStart + "-" + (amethyst / 5) + "% movement speed"));
                    }
                    if (amethyst == 100) tooltip.add(Component.literal(amethystStart + "Unbreakable"));
                }

                // Agility
                double agility = getImbueAmount(item, 4);
                String agilityStart = ModItems.imbueColors[4] + " * §7";
                if (agility > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(agilityStart+"+"+(agility*0.5)+"% attack speed"));
                    }
                    if (tool) {
                        if (agility >= 50) tooltip.add(Component.literal(agilityStart+"+"+((int)Math.floor(agility/25))+" levels of efficiency"));
                        else if (agility >= 25) tooltip.add(Component.literal(agilityStart+"+"+((int)Math.floor(agility/25))+" level of efficiency"));
                    }
                    if (armor) {
                        tooltip.add(Component.literal(agilityStart + "+" + (agility * 0.4) + "% movement speed"));
                    }
                }

                // Ancient
                double ancient = getImbueAmount(item, 5);
                String ancientStart = ModItems.imbueColors[5] + " * §7";
                if (ancient > 0) {
                    if (weapon) {
                        tooltip.add(Component.literal(ancientStart+"+"+(ancient)+"% experience gain"));
                    }
                    if (tool) {

                    }
                    if (armor) {
                        tooltip.add(Component.literal(ancientStart + "+" + (ancient * 0.5) + "% experience gain"));
                    }
                }
            } else {
                tooltip.add(Component.literal("§2[SHIFT] for imbued stats"));
            }
            tooltip.add(Component.literal(""));
        }
    }
}

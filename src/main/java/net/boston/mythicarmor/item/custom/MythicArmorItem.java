package net.boston.mythicarmor.item.custom;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static net.boston.mythicarmor.item.custom.MythicItem.addImbueTooltip;

public class MythicArmorItem extends DyeableArmorItem implements ColorableMythicItem {
    public MythicArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addImbueTooltip(item, pTooltipComponents);

        MythicItem.createDetailedImbueDetails(pTooltipComponents, item);

        super.appendHoverText(item, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        final Multimap<Attribute, AttributeModifier> superAttributes = super.getAttributeModifiers(slot, stack);

        if (stack.getItem() instanceof MythicArmorItem || stack.getItem() instanceof MythicElytraItem) {
            return MythicItemEffects.updateAttributeModifiers(superAttributes, slot, stack);
        }

        return superAttributes;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            List<ItemStack> equipment = List.of(helmet, chestplate, leggings, boots);
            if (equipment.stream().anyMatch(armor -> MythicItem.getImbueAmount(armor, 0) >= 100)) {
                if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) return;

                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }
        }
    }
}

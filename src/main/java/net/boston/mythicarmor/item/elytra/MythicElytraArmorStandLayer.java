package net.boston.mythicarmor.item.elytra;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.boston.mythicarmor.MythicArmor;
import net.boston.mythicarmor.item.ModItems;
import net.boston.mythicarmor.item.custom.MythicElytraItem;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicElytraArmorStandLayer extends ElytraLayer<ArmorStand, ArmorStandArmorModel> {
    private final ElytraModel<ArmorStand> elytraModel;
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation(MythicArmor.MOD_ID,
            "textures/entity/mythic_elytra.png");

    public MythicElytraArmorStandLayer(ArmorStandRenderer armorStandRenderer, EntityModelSet entityModelSet) {
        super(armorStandRenderer, entityModelSet);
        this.elytraModel = new ElytraModel<>(entityModelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public boolean shouldRender(ItemStack stack, ArmorStand entity) {
        return stack.getItem() == ModItems.MYTHIC_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, ArmorStand entity) {
        return TEXTURE_ELYTRA;
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, ArmorStand pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, pLivingEntity)) {
            ResourceLocation resourcelocation = getElytraTexture(itemstack, pLivingEntity);
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            int color = ((MythicElytraItem) itemstack.getItem()).getColor(itemstack);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(resourcelocation), false, itemstack.hasFoil());
            this.elytraModel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
            pMatrixStack.popPose();
        }
    }
}
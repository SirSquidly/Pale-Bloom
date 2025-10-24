package com.sirsquidly.palebloom.common.item.itemblock;

import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPalePumpkin extends ItemBlock
{
    protected static final ResourceLocation CREAKING_LANTERN_BLUR_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/misc/creaking_lantern_blur.png");
    protected static final ResourceLocation PALE_PUMPKIN_BLUR_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/misc/pale_pumpkin_blur.png");

    public ItemPalePumpkin(Block block)
    {
        super(block);
        this.setRegistryName(block.getRegistryName());

        this.addPropertyOverride(new ResourceLocation("night"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return (entityIn != null && WorldPaleGarden.isNight(entityIn.world)) ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack)
    {
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, net.minecraft.client.gui.ScaledResolution resolution, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(block == JTPGBlocks.CREAKING_LANTERN ? CREAKING_LANTERN_BLUR_TEXTURE : PALE_PUMPKIN_BLUR_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        if (ConfigCache.crkLtn_creakingIgnored && stack.getItem() == Item.getItemFromBlock(JTPGBlocks.CREAKING_LANTERN))
        { tooltip.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("description.palebloom.creaking_lantern.desc1")); }
    }
}
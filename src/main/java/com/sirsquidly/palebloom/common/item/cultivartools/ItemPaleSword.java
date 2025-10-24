package com.sirsquidly.palebloom.common.item.cultivartools;

import com.google.common.collect.Multimap;
import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPaleSword extends ItemSword
{
    boolean isNight;

    public ItemPaleSword(ToolMaterial material)
    {
        super(material);

        this.addPropertyOverride(new ResourceLocation("awake"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            { return isNight ? 1 : 0; }
        });
    }

    /** Called each Inventory Tick, same thing the Map does. This auto-sets the random sound if it doesn't have one.*/
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn.ticksExisted % 20 != 0) return;

        if (WorldPaleGarden.isNight(worldIn)) isNight = true;
        else isNight = false;

        if (ConfigCache.clvSwd_awakeBulbHealing && stack.getItemDamage() > 0)
        {
            if (WorldPaleGarden.requestBulbResin(worldIn, entityIn.getPosition().up(), 1, true).resinPulled == 1) this.setDamage(stack, this.getDamage(stack) - 1);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            if (isNight)
            {
                multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
                multimap.put( SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Night sword damage", ConfigCache.clvSwd_awakeDamage - 1.0D, 0));
            }
        }

        return multimap;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        if (!ConfigCache.clvSwd_awakeTransfer || !this.isNight) return super.hitEntity(stack, target, attacker);

        if (attacker.isPotionActive(MobEffects.POISON) || attacker.isPotionActive(MobEffects.WITHER))
        {
            if (attacker instanceof EntityPlayer)
            {
                if (((EntityPlayer)attacker).getCooldownTracker().getCooldown(stack.getItem(), 0) > 0) return super.hitEntity(stack, target, attacker);
                ((EntityPlayer)attacker).getCooldownTracker().setCooldown(stack.getItem(), ConfigCache.clvSwd_awakeTransferCooldown);
            }

            if (attacker.isPotionActive(MobEffects.POISON))
            {
                PotionEffect pot = attacker.getActivePotionEffect(MobEffects.POISON);
                target.addPotionEffect(pot);
                attacker.removePotionEffect(MobEffects.POISON);
            }
            if (attacker.isPotionActive(MobEffects.WITHER))
            {
                PotionEffect pot = attacker.getActivePotionEffect(MobEffects.WITHER);
                target.addPotionEffect(pot);
                attacker.removePotionEffect(MobEffects.WITHER);
            }

            for (int i = 0; i < 20; ++i)
            {
                double aX = attacker.posX + 0.5 + (attacker.world.rand.nextDouble() * 0.5 - 0.25);
                double aY = attacker.posY + 0.5 + (attacker.world.rand.nextDouble() * 0.5 - 0.25);
                double aZ = attacker.posZ + 0.5 + (attacker.world.rand.nextDouble() * 0.5 - 0.25);

                double tX = target.posX + (attacker.world.rand.nextDouble() * 0.5 - 0.25);
                double tY = target.posY + (attacker.world.rand.nextDouble() * target.height);
                double tZ = target.posZ + (attacker.world.rand.nextDouble() * 0.5 - 0.25);

                paleBloom.proxy.spawnParticle(0, target.world, aX, aY, aZ, tX, tY, tZ, 0, 2);
            }


            for (int i = 0; i < 10; i++)
            {
                double posX = target.posX + ((attacker.world.rand.nextDouble() * 2 - 1) * target.width * 0.5F);
                double posY = target.posY + (target.height * 0.5) + ((attacker.world.rand.nextDouble() * 2 - 1) * target.height);
                double posZ = target.posZ + ((attacker.world.rand.nextDouble() * 2 - 1) * target.width * 0.5F);


                double speedX = (attacker.world.rand.nextDouble() * 2 - 1) * 0.05F;
                double speedY = (attacker.world.rand.nextDouble() * 2 - 1) * 0.02F;
                double speedZ = (attacker.world.rand.nextDouble() * 2 - 1) * 0.05F;

                paleBloom.proxy.spawnParticle(2, attacker.world, posX, posY, posZ, speedX, speedY, speedZ, 2);
            }
        }

        return super.hitEntity(stack, target, attacker);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("");

        if (isNight)
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_title"));
            tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_damage", ConfigCache.clvSwd_awakeDamage - 5));
            if (ConfigCache.clvSwd_awakeBulbHealing) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.night_repair"));
            if (ConfigCache.clvSwd_awakeTransfer) tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_sword.night"));
        }
        else
        { tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.cultivar_item.day")); }

    }
}
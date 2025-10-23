package com.sirsquidly.palebloom.common.entity;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A separate abstract class utilized by the Creaking and Mannequin, because both
 * utilize many shared functions, while having other drastic differences.
 * */
public class AbstractCreaking extends EntityMob implements IEyeGlow
{
    public AbstractCreaking(World worldIn) { super(worldIn); }

    @Override
    protected void collideWithEntity(Entity entityIn)
    {
        if (!getSEEN()) super.collideWithEntity(entityIn);
        else if (!this.isRidingSameEntity(entityIn) && !entityIn.noClip && !this.noClip)
        {
            double d0 = entityIn.posX - this.posX;
            double d1 = entityIn.posZ - this.posZ;
            double d2 = MathHelper.absMax(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = (double)MathHelper.sqrt(d2);
                d0 = d0 / d2;
                d1 = d1 / d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d0 = d0 * d3;
                d1 = d1 * d3;
                d0 = d0 * 0.05000000074505806D;
                d1 = d1 * 0.05000000074505806D;
                d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                d1 = d1 * (double)(1.0F - this.entityCollisionReduction);


                if (!entityIn.isBeingRidden())
                { entityIn.addVelocity(d0, 0.0D, d1); }
            }
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn)
    { if (!getSEEN()) super.applyEntityCollision(entityIn); }

    /**
     * Checks if any Players within 16 blocks can see this Entity, and sets the `isSeen` value.
     *
     * Creative and players wearing Pumpkins are ignored.
     * */
    public void updateSight()
    {
        List<Entity> checkPlayerSight = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(16));

        if(!checkPlayerSight.isEmpty())
        {
            for (Entity player: checkPlayerSight)
            {
                ItemStack itemstack = ((EntityPlayer) player).inventory.armorInventory.get(3);

                if (this.isSeen((EntityPlayer)player) && !((EntityPlayer) player).isCreative() && !((EntityPlayer) player).isSpectator() && !isWornItemVisionImparing(itemstack.getItem()))
                {
                    if (!this.getSEEN()) this.playSound(getFreezeSound(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);

                    this.setSEEN(true);
                    this.navigator.clearPath();
                    return;
                }
            }
        }

        if (this.getSEEN()) this.playSound(getUnfreezeSound(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
        this.setSEEN(false);
    }

    /* Returns if the player is wearing any type of Pumpkin. */
    public boolean isWornItemVisionImparing(Item item)
    {
        return item == Item.getItemFromBlock(Blocks.PUMPKIN) || item == Item.getItemFromBlock(JTPGBlocks.PALE_PUMPKIN) || item == Item.getItemFromBlock(JTPGBlocks.CREAKING_LANTERN);
    }

    public boolean isSeen(EntityPlayer player)
    {
        Vec3d playerLook = player.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (this.height / 2.0) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = playerLook.dotProduct(vec3d1);
        return d1 > 0.5D && player.canEntityBeSeen(this);
    }

    /** Whenever the Attack Target gets updated to something new, play a targeting sound. */
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase attackTarget)
    {
        /* Do not play any sounds if the target is the exact same as prior! */
        if (attackTarget != this.getAttackTarget())
        {
            if (attackTarget != null)
            {
                this.playSound(getTargetSound(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                this.setGlowingEyes(true);
            }
            else
            {
                this.playSound(getUntargetSound(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                this.setGlowingEyes(false);
            }
        }

        super.setAttackTarget(attackTarget);
    }

    protected SoundEvent getFreezeSound() { return null; }
    protected SoundEvent getUnfreezeSound() { return null; }

    protected SoundEvent getTargetSound() { return null; }
    protected SoundEvent getUntargetSound() { return null; }

    public boolean getSEEN() { return false; }
    public void setSEEN(boolean bool) { }

    public ResourceLocation getEyeGlowTexture() { return null; }
    public boolean getGlowingEyes() { return false; }
    public void setGlowingEyes(boolean bool) {}
}
package com.sirsquidly.creaturesfromdarkness.entity;

import com.sirsquidly.creaturesfromdarkness.Config;
import com.sirsquidly.creaturesfromdarkness.init.CFDItems;
import com.sirsquidly.creaturesfromdarkness.init.CFDLootTables;
import com.sirsquidly.creaturesfromdarkness.init.CFDSounds;
import com.sirsquidly.creaturesfromdarkness.entity.ai.EntityAIPlayerStalk;
import com.sirsquidly.creaturesfromdarkness.entity.ai.EntityAISeekBreakLight;
import net.minecraft.block.Block;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityShadow extends AbstractShadowMob
{
    private final EntityAIPlayerStalk aiPlayerStalk = new EntityAIPlayerStalk(this, 1.0D);
    private final EntityAIAttackMelee aiAttackMelee = new EntityAIAttackMelee(this, 1.0D, false);
    /** If this Shadow was spawned by a Nightmare. */
    public boolean nightmareSpawn;

    /** If this Nightmare is fully hostile and tries to attack. */
    private boolean whiteEyed;

    public EntityShadow(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 5;
        this.setBaseAi();
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAISeekBreakLight(this, 1.0D, 1.5D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI()
    { this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false)); }

    protected int getExperiencePoints(EntityPlayer player)
    { return nightmareSpawn ? this.experienceValue = 0 : super.getExperiencePoints(player); }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
    }

    /** Many Ai components are made to be toggled based on other factors, thus need to be setup differently! */
    public void setBaseAi()
    {
        if (this.world != null && !this.world.isRemote)
        {
            this.tasks.addTask(4, this.aiPlayerStalk);
        }
    }

    /** Reacts based on what the look reaction should be*/
    public void preformSeeReaction(EntityPlayer player)
    {
        //this.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 2, 0, true, false));
        if (this.world != null && !this.world.isRemote)
        {
            this.playSound(CFDSounds.ENTITY_SHADOW_HURT, 1.0F, 0.4F);
            this.tasks.addTask(1, this.aiAttackMelee);
            this.tasks.removeTask(this.aiPlayerStalk);
            setSEEN(true);
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return CFDSounds.ENTITY_SHADOW_HURT; }

    protected SoundEvent getDeathSound() { return CFDSounds.ENTITY_SHADOW_DEATH; }

    /** No step sounds if invisible! */
    @Override
    public void playStepSound(BlockPos pos, Block blockIn)
    { if (getCurrentLight() > 5) super.playStepSound(pos, blockIn); }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return CFDLootTables.SHADOW_DROPS;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                entityplayer.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
                this.setDead();
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setLeftHanded(this.rand.nextFloat() < 0.05F);

        if (this.rand.nextFloat() < 0.01F)
        { this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CFDItems.NIGHTMARE_LEG)); }
        this.setWhitEeyed(true);

        return livingdata;
    }


    @Override
    public boolean getCanSpawnHere()
    {
        int configYLevel = Config.entity.shadow.shadowSpawning.maxYLevel;

        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && getCurrentLight() <= Config.entity.shadow.shadowSpawning.maxLightLevel && (configYLevel == 0 || this.posY < configYLevel))
        { return super.getCanSpawnHere(); }

        return false;
    }

    public boolean getWhiteEyed()
    { return this.whiteEyed; }

    public void setWhitEeyed(boolean whiteEyedIn)
    { this.whiteEyed = whiteEyedIn; }

    /** No collisions. */
    @Override
    public void collideWithNearbyEntities() {}
    @Override
    public boolean canBePushed()
    { return false; }

    protected boolean canDropLoot()
    { return true; }

    public boolean isPreventingPlayerRest(EntityPlayer playerIn)
    { return true; }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("HasWhiteEyes", this.getWhiteEyed());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("HasWhiteEyes", 1)) this.setWhitEeyed(compound.getBoolean("HasWhiteEyes"));
    }
}
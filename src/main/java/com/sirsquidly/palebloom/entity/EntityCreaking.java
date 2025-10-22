package com.sirsquidly.palebloom.entity;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.blocks.tileentity.TileCreakingHeart;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenMelee;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenSwimming;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenWander;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenWatching;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGLootTables;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import com.sirsquidly.palebloom.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;


public class EntityCreaking extends AbstractCreaking implements IAnimatable, IAnimationTickable
{
    private static final ResourceLocation EYEGLOW_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/creaking/creaking_e.png");


    protected static final DataParameter<Byte> ANIM_TICK = EntityDataManager.createKey(EntityCreaking.class, DataSerializers.BYTE);
    /** True when the Creaking is being seen by its target. */
    private static final DataParameter<Boolean> IS_SEEN = EntityDataManager.createKey(EntityCreaking.class, DataSerializers.BOOLEAN);
    /** If this Creaking has a Heart it is spawned from, and where that position is. */
    protected static final DataParameter<BlockPos> HOME_HEART_POS = EntityDataManager.createKey(EntityCreaking.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Byte> HURT_TIME = EntityDataManager.createKey(EntityCreaking.class, DataSerializers.BYTE);
    /** If the Creaking's eyes should be glowing. */
    private static final DataParameter<Boolean> EYES_GLOW = EntityDataManager.createKey(EntityCreaking.class, DataSerializers.BOOLEAN);

    protected static final AnimationBuilder IDLE_ANIMATION = new AnimationBuilder().addAnimation("idle", false);
    protected static final AnimationBuilder WALK_ANIMATION = new AnimationBuilder().addAnimation("walk", true);
    protected static final AnimationBuilder HIT_ANIMATION = new AnimationBuilder().addAnimation("hit", false);

    protected static final AnimationBuilder TWITCH_ANIMATION = new AnimationBuilder().addAnimation("twitch", false);
    protected static final AnimationBuilder ATTACK_ANIMATION = new AnimationBuilder().addAnimation("attack", false);

    public int attackTick;

    private static final double configAttackDamage = Config.entity.creaking.attackDamage;
    private static final double configChaseSpeedMult = Config.entity.creaking.chaseSpeedMult;

    public EntityCreaking(World worldIn)
    {
        super(worldIn);
        this.setSize(0.9F, 2.7F);
        this.stepHeight = 1.0F;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(ANIM_TICK, (byte) 0);
        this.getDataManager().register(IS_SEEN, false);
        this.getDataManager().register(EYES_GLOW, false);
        this.getDataManager().register(HOME_HEART_POS, BlockPos.ORIGIN);
        this.getDataManager().register(HURT_TIME, (byte) 0);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAIUnseenSwimming(this));
        this.tasks.addTask(1, new EntityAIUnseenMelee(this, configChaseSpeedMult, false, true));
        this.tasks.addTask(5, new EntityAIUnseenWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIUnseenWatching(this, EntityPlayer.class, 8.0F));

        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
        /* Speed need to get raised to 0.12D when chasing. */
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.09D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(configAttackDamage);
    }

    protected ResourceLocation getLootTable() { return JTPGLootTables.ENTITIES_CREAKING; }

    protected void playStepSound(BlockPos pos, Block blockIn) { this.playSound(JTPGSounds.ENTITY_CREAKING_STEP, 0.15F, 1.0F); }

    protected SoundEvent getAmbientSound() { return JTPGSounds.ENTITY_CREAKING_AMBIENT; }

    protected SoundEvent getFreezeSound() { return JTPGSounds.ENTITY_CREAKING_FREEZE; }
    protected SoundEvent getUnfreezeSound() { return JTPGSounds.ENTITY_CREAKING_UNFREEZE; }

    protected SoundEvent getTargetSound() { return JTPGSounds.ENTITY_CREAKING_TARGET; }
    protected SoundEvent getUntargetSound() { return JTPGSounds.ENTITY_CREAKING_NOTARGET; }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.updateSight();

        if (!this.isImmuneToFire && this.spawnedByHeart()) this.isImmuneToFire = true;
        if (this.isSwingInProgress)
        {
            this.playSound(JTPGSounds.ENTITY_CREAKING_ATTACK, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            this.attackTick = 20;
            this.isSwingInProgress = false;
        }

        int twitch = this.getAnimTick();
        if (twitch > 0)
        {
            this.setAnimTick(--twitch);
            this.navigator.clearPath();

            this.setGlowingEyes((twitch > 40 || twitch <= 36) && (twitch > 28 || twitch <= 25) && (twitch > 20 || twitch <= 18) && (twitch > 14 || twitch <= 12) && (twitch > 8 || twitch <= 6));

            if (twitch <= 1)
            { preformDeathEffects(); }
        }

        if (this.getHurtTime() > 0) setHurtTime((byte) (this.getHurtTime() - 1));

        --this.attackTick;
    }

    //public boolean isImmuneToFire()

    /** If this Creaking is from a Heart, there is no need to deal real damage. */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entitySource = source.getTrueSource();

        if (entitySource instanceof EntityLivingBase && !world.isRemote) WorldPaleGarden.alertReapingWillow(this.world, this.getPosition(), (EntityLivingBase)entitySource, 16);

        if (this.spawnedByHeart() && !source.canHarmInCreative())
        {
            if (entitySource instanceof EntityLivingBase) this.setRevengeTarget((EntityLivingBase)entitySource);

            if (this.getHurtTime() <= 0)
            {
                this.playSound(JTPGSounds.ENTITY_CREAKING_SWAY, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                this.setHurtTime((byte) 10);

                /* This communicates with the Tile Entity, to preform associated functions. */
                TileEntity tileentity = world.getTileEntity(this.getHeartPos());
                if (tileentity instanceof TileCreakingHeart) ((TileCreakingHeart) tileentity).preformHitReact(source);
            }

            return false;
        }

        super.attackEntityFrom(source, amount);
        return true;
    }

    /** Creakings do not target players wearing a Creaking Lantern. */
    @Override
    public void setAttackTarget(@Nullable EntityLivingBase attackTarget)
    {
        if (attackTarget instanceof EntityPlayer)
        {
            ItemStack itemstack = ((EntityPlayer) attackTarget).inventory.armorInventory.get(3);
            if (itemstack.getItem() == Item.getItemFromBlock(JTPGBlocks.CREAKING_LANTERN) && attackTarget != this.getRevengeTarget()) return;
        }

        super.setAttackTarget(attackTarget);
    }

    /** Begins the long death for the Creaking. 43 is the exact number for a normal death. */
    public void preformTwitchingDeath() { preformTwitchingDeath(43); }
    public void preformTwitchingDeath(int ticksIn)
    {
        if (ticksIn != 1) this.playSound(JTPGSounds.ENTITY_CREAKING_TWITCH, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
        this.setAnimTick(ticksIn);
    }

    /** Spawns Particles and such for the special Creaking Death.
     *
     * This is separate from a `onDeathUpdate()` override since a Creaking CAN die normally, if they are not attached to a Creaking Heart.
     * */
    public void preformDeathEffects()
    {
        this.playSound(JTPGSounds.ENTITY_CREAKING_DEATH, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);

        if (this.world.isRemote)
        {
            for (int i = 0; i < 64; ++i)
            {
                double d0 = this.posX + (this.rand.nextDouble() * 0.5 - 0.25);
                double d1 = this.posY + (this.rand.nextDouble() * this.height);
                double d2 = this.posZ + (this.rand.nextDouble() * 0.5 - 0.25);
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, d0, d1, d2, 0, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, 0, Block.getStateId(JTPGBlocks.CREAKING_HEART.getDefaultState()));
            }
        }

        if (this.getLastAttackedEntity() instanceof EntityPlayer) this.getLastAttackedEntity().onKillEntity(this);

        this.setDead();
    }

    public int getAnimTick() { return this.dataManager.get(ANIM_TICK); }
    public void setAnimTick(int byteIn) { this.dataManager.set(ANIM_TICK, (byte) byteIn); }

    public ResourceLocation getEyeGlowTexture() { return EYEGLOW_TEXTURE; }
    public boolean getGlowingEyes() { return this.dataManager.get(EYES_GLOW); }
    public void setGlowingEyes(boolean bool) { this.dataManager.set(EYES_GLOW, bool); }

    public boolean getSEEN() { return this.dataManager.get(IS_SEEN); }
    public void setSEEN(boolean bool) { this.dataManager.set(IS_SEEN, bool); }

    public void setHeartPos(BlockPos pos) { this.dataManager.set(HOME_HEART_POS, pos); }
    public BlockPos getHeartPos() { return this.dataManager.get(HOME_HEART_POS); }

    public byte getHurtTime() { return this.dataManager.get(HURT_TIME); }
    public void setHurtTime(byte timeSinceHit) { this.dataManager.set(HURT_TIME, timeSinceHit); }

    /** A boolean to see if this Creaking was spawned by a Creaking Heart. */
    public boolean spawnedByHeart() { return this.getHeartPos() != BlockPos.ORIGIN; }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("EyesGlow", getGlowingEyes());
        compound.setBoolean("Seen", getSEEN());

        BlockPos blockpos = this.getHeartPos();
        compound.setInteger("HeartPosX", blockpos.getX());
        compound.setInteger("HeartPosY", blockpos.getY());
        compound.setInteger("HeartPosZ", blockpos.getZ());

        compound.setInteger("TwitchTick", getAnimTick());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setGlowingEyes(compound.getBoolean("EyesGlow"));
        this.setSEEN(compound.getBoolean("Seen"));

        int x = compound.getInteger("HeartPosX");
        int y = compound.getInteger("HeartPosY");
        int z = compound.getInteger("HeartPosZ");
        this.dataManager.set(HOME_HEART_POS, new BlockPos(x, y, z));

        this.setAnimTick(compound.getInteger("TwitchTick"));
    }

    /** Ahead is GeckoLib implimentations! */
    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData)
    {
        animationData.shouldPlayWhilePaused = true;
        animationData.addAnimationController(new AnimationController(this, "walking_controller", 2, this::predicateWalking));
        animationData.addAnimationController(new AnimationController(this, "hurt_controller", 1, this::predicateHurt));
        //animationData.addAnimationController(new AnimationController(this, "twitch_controller", 1, this::predicateTwitch));
    }

    private <E extends IAnimatable> PlayState predicateWalking(AnimationEvent<E> event)
    {
        if (event.getLimbSwingAmount() > 0.005F && !this.getSEEN())
        {
            event.getController().setAnimation(WALK_ANIMATION);

            /* Speed adjustment is based around keeping it close to 1 at the default walking speed, but going far higher at high speeds. */
            event.getController().setAnimationSpeed(event.getLimbSwingAmount() * 2 + 0.88f);
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(IDLE_ANIMATION);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateHurt(AnimationEvent<E> event)
    {
        if (this.getAnimTick() > 0)
        {
            event.getController().setAnimation(TWITCH_ANIMATION);
            return PlayState.CONTINUE;
        }
        else if (this.getHurtTime() > 0)
        {
            event.getController().setAnimation(HIT_ANIMATION);
            return PlayState.CONTINUE;
        }
        else if (this.attackTick > 0)
        {
            event.getController().setAnimation(ATTACK_ANIMATION);
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() { return this.factory; }

    @Override
    public void tick() {}

    @Override
    public int tickTimer()
    { return this.ticksExisted; }
}
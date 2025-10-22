package com.sirsquidly.palebloom.entity;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenMelee;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenWander;
import com.sirsquidly.palebloom.entity.ai.EntityAIUnseenWatching;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGLootTables;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityMannequin extends AbstractCreaking
{
    private static final ResourceLocation EYEGLOW_TEXTURE = new ResourceLocation(paleBloom.MOD_ID, "textures/entities/mannequin/mannequin_e.png");

    /** True when the Creaking is being seen by its target. */
    private static final DataParameter<Boolean> IS_SEEN = EntityDataManager.createKey(EntityMannequin.class, DataSerializers.BOOLEAN);
    /** If the Creaking's eyes should be glowing. */
    private static final DataParameter<Boolean> EYES_GLOW = EntityDataManager.createKey(EntityMannequin.class, DataSerializers.BOOLEAN);
    /** If a Mannequin is inactive. */
    private static final DataParameter<Boolean> INACTIVE = EntityDataManager.createKey(EntityMannequin.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Byte> HURT_TIME = EntityDataManager.createKey(EntityMannequin.class, DataSerializers.BYTE);

    private static final double configChaseSpeedMult = Config.entity.creaking.chaseSpeedMult;

    public int attackTick;
    public long punchCooldown;

    public float cachedLimbSwing;
    public float cachedLimbSwingAmount;
    public float cachedAgeInTicks;
    public float cachedSwingProgress;

    public boolean frozenPose;

    public EntityMannequin(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.stepHeight = 1.0F;
    }

    public EntityMannequin(World worldIn, double posX, double posY, double posZ, float rotation)
    {
        this(worldIn);
        this.setPosition(posX, posY, posZ);

        this.rotationYaw = rotation;
        this.prevRotationYaw = rotation;
        this.renderYawOffset = rotation;
        this.prevRenderYawOffset = rotation;
        this.rotationYawHead = rotation;
        this.prevRotationYawHead = rotation;

        this.randomYawVelocity = 0;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(EYES_GLOW, false);
        this.getDataManager().register(HURT_TIME, (byte) 0);
        this.getDataManager().register(IS_SEEN, false);
        this.getDataManager().register(INACTIVE, false);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIUnseenMelee(this, configChaseSpeedMult, false, true));
        this.tasks.addTask(2, new EntityAIUnseenWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIUnseenWatching(this, EntityPlayer.class, 8.0F));
        //this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        /* Speed need to get raised to 0.12D when chasing. */
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0F);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    protected ResourceLocation getLootTable() { return JTPGLootTables.ENTITIES_MANNEQUIN; }

    protected void playStepSound(BlockPos pos, Block blockIn) { this.playSound(JTPGSounds.ENTITY_CREAKING_STEP, 0.15F, 1.0F); }

    protected SoundEvent getAmbientSound() { return null; }

    public void onLivingUpdate()
    {
        this.updateSight();

        if (getSEEN())
        {
            if (!frozenPose)
            {
                cachedLimbSwing = limbSwing;
                cachedLimbSwingAmount = limbSwingAmount;
                cachedAgeInTicks = ticksExisted;
                cachedSwingProgress = this.swingProgress;
                frozenPose = true;
            }

            this.renderYawOffset = this.rotationYawHead;
        }
        else
        {
            frozenPose = false;
        }

        if (getHurtTime() > 0) this.setHurtTime((byte) (getHurtTime() - 1));

        --this.attackTick;

        super.onLivingUpdate();
    }

    /* NO XP DROP, if Inactive. Since it isn't alive. */
    protected int getExperiencePoints(EntityPlayer player) { return this.getInactive() ? 0 : this.experienceValue; }

    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
    {
        ItemStack playerItem = player.getHeldItem(EnumHand.MAIN_HAND);

        if (player.isSpectator()) return EnumActionResult.FAIL;

        if (playerItem.isEmpty())
        {
            EntityEquipmentSlot entityequipmentslot1 = this.getClickedSlot(vec);

            if (this.hasItemInSlot(entityequipmentslot1))
            {
                player.swingArm(EnumHand.MAIN_HAND);
                performEquipmentSwap(player, playerItem, entityequipmentslot1);
                return EnumActionResult.SUCCESS;
            }
        }
        else
        {
            EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(playerItem);

            player.swingArm(EnumHand.MAIN_HAND);
            performEquipmentSwap(player, playerItem, slot);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }


    /**
     * Preforms the swapping of items, including dropping any currently held items
     * Drowned Shipmates can only utilize their head and hand slots, so we only check those!
     * */
    public void performEquipmentSwap(EntityPlayer player, ItemStack playerItem, EntityEquipmentSlot slot)
    {
        if (!this.getItemStackFromSlot(slot).isEmpty() && !world.isRemote)
        { this.entityDropItem(this.getItemStackFromSlot(slot), 1.0F); }

        ItemStack itemCopy = playerItem.copy();
        itemCopy.setCount(1);
        this.setItemStackToSlot(slot, itemCopy);

        this.setDropChance(slot, 100);
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F);

        if (!player.capabilities.isCreativeMode) { playerItem.shrink(1); }
    }

    protected EntityEquipmentSlot getClickedSlot(Vec3d vector)
    {
        EntityEquipmentSlot entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        double y = vector.y;
        EntityEquipmentSlot entityequipmentslot1 = EntityEquipmentSlot.FEET;


        if (y >= 0.1D && y < 0.1D + 0.45D && this.hasItemInSlot(entityequipmentslot1))
        {
            entityequipmentslot = EntityEquipmentSlot.FEET;
        }
        else if (y >= 0.4D && y < 0.4D + 0.8D && this.hasItemInSlot(EntityEquipmentSlot.LEGS))
        {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        if (y >= 0.7D && y < 0.7D + 0.2D && ( hasItemInSlot(EntityEquipmentSlot.MAINHAND) || hasItemInSlot(EntityEquipmentSlot.OFFHAND)))
        {
            return hasItemInSlot(EntityEquipmentSlot.MAINHAND) ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND;
        }
        else if (y >= 0.9D + 0.0D && y < 0.9D + 0.7D && this.hasItemInSlot(EntityEquipmentSlot.CHEST))
        {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (y >= 1.6D && this.hasItemInSlot(EntityEquipmentSlot.HEAD))
        {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }

        return entityequipmentslot;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.world.isRemote && !this.isDead)
        {
            if (DamageSource.OUT_OF_WORLD.equals(source))
            {
                this.setDead();
                return false;
            }
            else if (!this.isEntityInvulnerable(source))
            {
                if (source.getTrueSource() instanceof EntityPlayer)
                {
                    if (!((EntityPlayer)source.getTrueSource()).capabilities.allowEdit) return false;

                    else if (source.isCreativePlayer())
                    {
                        preformDeathEffects(source, false);
                        return false;
                    }
                    else
                    {
                        long i = this.world.getTotalWorldTime();

                        if (i - this.punchCooldown > 4)
                        {
                            this.playSound(JTPGSounds.ENTITY_MANNEQUIN_SWAY, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                            setHurtTime((byte) 10);
                            this.world.setEntityState(this, (byte)32);
                            this.punchCooldown = i;
                        }
                        else
                        { preformDeathEffects(source, true); }

                        return false;
                    }
                }

                this.damageEntity(source, amount);

                if (this.getHealth() > 0)
                {
                    long i = this.world.getTotalWorldTime();

                    if (i - this.punchCooldown > 4)
                    {
                        this.playSound(JTPGSounds.ENTITY_MANNEQUIN_SWAY, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
                        setHurtTime((byte) 10);
                        this.world.setEntityState(this, (byte)32);
                        this.punchCooldown = i;
                    }
                    else
                    { preformDeathEffects(source, true); }
                }

                return false;
            }
            else
            { return false; }
        }
        else
        { return false; }
    }

    public void preformDeathEffects(DamageSource source, boolean doDrops)
    {
        if (doDrops)
        {
            this.onDeath(source);

            if (!source.isCreativePlayer())
            {
                int xp = this.getExperiencePoints(null);
                while (xp > 0)
                {
                    int split = EntityXPOrb.getXPSplit(xp);
                    xp -= split;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, split));
                }
            }
        }
        this.playSound(JTPGSounds.ENTITY_MANNEQUIN_BREAK, 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
        this.playParticles(JTPGBlocks.PALE_OAK_PLANKS.getDefaultState(), 10);
        if (!this.getInactive())
        {
            this.playParticles(JTPGBlocks.RESIN_BLOCK.getDefaultState(), 3);
        }
        this.isDead = true;
        this.setHealth(0.0F);
        this.setDead();
    }

    private void playParticles(IBlockState state, int particleQuantity)
    {
        if (this.world instanceof WorldServer)
        {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, particleQuantity, (double)(this.width / 4.0F), (double)(this.height / 4.0F), (double)(this.width / 4.0F), 0.05D, Block.getStateId(state));
        }
    }

    /** Mannequins are created, so do not despawn. */
    protected boolean canDespawn() { return false; }

    protected int decreaseAirSupply(int air)
    {
        return air;
    }

    public boolean isPreventingPlayerRest(EntityPlayer playerIn)
    {
        return false;
    }

    /** Inactive Mannequins are just ones which always believe they are being seen. */
    @Override
    public void updateSight() { if (!this.getInactive()) super.updateSight(); }

    public ResourceLocation getEyeGlowTexture() { return EYEGLOW_TEXTURE; }
    public boolean getGlowingEyes() { return this.dataManager.get(EYES_GLOW); }
    public void setGlowingEyes(boolean bool) { this.dataManager.set(EYES_GLOW, bool); }

    public boolean getSEEN() { return this.dataManager.get(IS_SEEN); }
    public void setSEEN(boolean bool) { this.dataManager.set(IS_SEEN, bool); }

    public boolean getInactive() { return this.dataManager.get(INACTIVE); }
    public void setInactive(boolean bool) { this.dataManager.set(INACTIVE, bool); }

    public byte getHurtTime() { return this.dataManager.get(HURT_TIME); }
    public void setHurtTime(byte timeSinceHit) { this.dataManager.set(HURT_TIME, timeSinceHit); }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Inactive", getInactive());
        compound.setBoolean("EyesGlow", getGlowingEyes());
        compound.setBoolean("Seen", getSEEN());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setInactive(compound.getBoolean("Inactive"));
        this.setGlowingEyes(compound.getBoolean("EyesGlow"));
        this.setSEEN(compound.getBoolean("Seen"));
    }
}
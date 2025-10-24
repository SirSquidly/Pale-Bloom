package com.sirsquidly.palebloom.common;

import com.sirsquidly.palebloom.common.entity.EntityMannequin;
import com.sirsquidly.palebloom.common.entity.EntityPaleCreeper;
import com.sirsquidly.palebloom.common.entity.item.EntityThorn;
import com.sirsquidly.palebloom.common.item.cultivartools.ItemPaleAxe;
import com.sirsquidly.palebloom.config.ConfigCache;
import com.sirsquidly.palebloom.init.JTPGBiomes;
import com.sirsquidly.palebloom.init.JTPGItems;
import com.sirsquidly.palebloom.common.item.ItemPaleMossCloak;
import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEvents
{
    private static final boolean configPaleCloakEnabled = Config.item.paleMossCloak.enablePaleMossCloak;
    private static final boolean configPaleCloakReapingDefends = Config.item.paleMossCloak.reapingWillowsDefendCloakWearers;

    /** Hitting the ground with a Pale Shovel throws Thorns. */
    @SubscribeEvent
    public static void onLeftClickWithPaleShovel(PlayerInteractEvent.LeftClickBlock event)
    {
        if (!ConfigCache.ctvSvl_awakeThornStroke || event.getWorld().isRemote || event.getFace() != EnumFacing.UP ) return;

        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPaleAxe)
        {
            if (!WorldPaleGarden.isNight(event.getWorld())) return;

            EntityPlayer player = event.getEntityPlayer();
            if (player.getCooldownTracker().getCooldown(stack.getItem(), 0) > 0) return;
            player.getCooldownTracker().setCooldown(stack.getItem(), ConfigCache.ctvSvl_awakeThornStrokeCooldown);

            stack.damageItem(1, player);

            ((WorldServer)player.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, event.getHitVec().x, event.getHitVec().y, event.getHitVec().z, 10, 0.1F, 0.1F, 0.1F, 0.05D, Block.getStateId(player.world.getBlockState(event.getPos())));

            for (int i = 0; i < 6; i++)
            {
                EntityThorn thorn = new EntityThorn(player.world, event.getHitVec().x, event.getHitVec().y, event.getHitVec().z, player);

                float f = -MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);
                float f1 = MathHelper.sin((player.rotationPitch * 0.4F) * 0.017453292F);
                float f2 = MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F);

                thorn.shoot(f, f1, f2, 0.5F, 10);

                event.getWorld().spawnEntity(thorn);
            }
        }
    }

    @SubscribeEvent
    public static void spawnCreeperEvent(EntityJoinWorldEvent event)
    {
        Entity spawn = event.getEntity();

        if(spawn instanceof EntityCreeper && !spawn.world.isRemote)
        {
            if (spawn.world.rand.nextDouble() <= (Config.entity.paleCreeper.paleCreeperReplacementChance * 0.01D) && !spawn.getTags().contains("DidPaleReplacementCheck") && spawn.world.getBiome(spawn.getPosition()) == JTPGBiomes.PALE_GARDEN)
            {
                EntityPaleCreeper paleCreeper = new EntityPaleCreeper(spawn.world);
                paleCreeper.setLocationAndAngles(spawn.posX, spawn.posY, spawn.posZ, spawn.rotationYaw, 0.0F);
                spawn.world.spawnEntity(paleCreeper);
                spawn.setDead();
            }
            spawn.addTag("DidPaleReplacementCheck");
        }
    }

    /** Prevent the Mannequin from being targeted. */
    @SubscribeEvent
    public static void noMannequinTargeting(LivingSetAttackTargetEvent event)
    {
        if (event.getTarget() == null || !(event.getEntity() instanceof EntityLiving)) return;

        if (event.getTarget() instanceof EntityMannequin) ((EntityLiving)event.getEntity()).setAttackTarget(null);
    }

    /** Creepers are Neutral to entities wearing a Pale Moss Cloak. */
    @SubscribeEvent
    public static void onCreeperTargetMossCloakWearer(LivingSetAttackTargetEvent event)
    {
        if (!configPaleCloakEnabled) return;
        if (event.getTarget() == null || !(event.getEntity() instanceof EntityCreeper) || event.getEntityLiving().getRevengeTarget() == event.getTarget()) return;

        ItemStack stack = event.getTarget().getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (stack.getItem() == JTPGItems.PALE_MOSS_CLOAK && ((ItemPaleMossCloak)stack.getItem()).hasSlottedAbility(stack, "pale_creeper"))
        { ((EntityCreeper) event.getEntity()).setAttackTarget(null); }
    }

    /** Creepers are Neutral to entities wearing a Pale Moss Cloak. */
    @SubscribeEvent
    public static void onMossCloakWearerHarmed(LivingHurtEvent event)
    {
        if (!configPaleCloakEnabled || !configPaleCloakReapingDefends) return;

        Entity entity = event.getSource().getTrueSource();
        if (entity == null || entity.isDead || event.getSource().isCreativePlayer() || !(entity instanceof EntityLivingBase)) return;
        ItemStack stack = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (stack.getItem() != JTPGItems.PALE_MOSS_CLOAK) return;
        if (event.getSource().getTrueSource() == event.getEntityLiving()) return;

        WorldPaleGarden.alertReapingWillow(event.getEntity().world, event.getEntity().getPosition(), (EntityLivingBase)entity, 16);

        if (((ItemPaleMossCloak)stack.getItem()).hasSlottedAbility(stack, "bramble"))
        {
            entity.attackEntityFrom(DamageSource.causeThornsDamage(event.getEntityLiving()), 3.0F);
        }

        if (((ItemPaleMossCloak)stack.getItem()).hasSlottedAbility(stack, "creaking_heart"))
        {
            float damageToPrevent = event.getAmount() * 0.5F;
            WorldPaleGarden.BulbPullResults result = WorldPaleGarden.requestBulbResin(event.getEntity().world, event.getEntityLiving().getPosition(), (int) damageToPrevent);

            for (BlockPos bulbPos : result.bulbsContributing)
            { WorldPaleGarden.spawnCreakingTrailParticles(event.getEntityLiving(), bulbPos, result.resinPulled * 2, 2); }

            event.setAmount(event.getAmount() - result.resinPulled);
        }
    }
}
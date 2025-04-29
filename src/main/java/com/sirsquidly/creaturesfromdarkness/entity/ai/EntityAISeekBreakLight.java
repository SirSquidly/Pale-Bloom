package com.sirsquidly.creaturesfromdarkness.entity.ai;

import com.sirsquidly.creaturesfromdarkness.entity.EntityShadow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;

/**
 * Used for detecting a nearby light source, moving to it, breaking it, then running away
 * */
public class EntityAISeekBreakLight extends EntityAIBase
{
    /** Defines the specific state of the behavior.
     * 0 = none, 1 = moving to light, 2 = fleeing * */
    private int state;

    private final EntityCreature creature;
    private final double movementSpeed;
    /** How fast to run away */
    private final double scatterSpeed;
    /** The position of the Block this Entity is attempting to reach */
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    /** Used so the searching for light sources is on a delay, rather than constant math */
    protected int actionDelay;
    /** Corrects the pathing live */
	private int timeoutCounter;
    /** How far in blocks to check for a Light */
    private final int searchLength;

    public EntityAISeekBreakLight(EntityCreature creatureIn, double speedIn, double scatterSpeedIn)
    {
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
        this.scatterSpeed = scatterSpeedIn;
        this.searchLength = 16;
        this.setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature)) return false;

        return this.creature.world.getLightFor(EnumSkyBlock.BLOCK, this.creature.getPosition()) > 5;
    }

    //public boolean shouldContinueExecuting() { return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock); }

    public void updateTask()
    {
        switch (state)
        {
            case 0:
                if (--this.actionDelay < 0)
                {
                    this.actionDelay = 10;
                    this.findViableLightTarget();
                }
                if (this.destinationBlock != BlockPos.ORIGIN) state = 1;
                break;
            case 1:
                this.creature.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY()), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.creature.getVerticalFaceSpeed());

                if (this.creature.getDistanceSqToCenter(this.destinationBlock.up()) > 3.0D)
                {
                    if (this.creature.getNavigator().noPath())
                    {
                        this.creature.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
                    }

                    /* Re-check the position occasionally to make sure it's a good idea to go there still. */
                    if (--this.actionDelay < 0)
                    {
                        this.actionDelay = 10;
                        /* If the destination is no longer good, just try and retreat. */
                        if (!this.shouldMoveTo(this.creature.world, this.destinationBlock)) state = 2;


                        List<Entity> checkPlayerSight = this.creature.world.getEntitiesWithinAABB(EntityPlayer.class, creature.getEntityBoundingBox().grow(20));

                        if(!checkPlayerSight.isEmpty())
                        {
                            for (Entity player: checkPlayerSight)
                            {
                                //if (this.creature.isSeen((EntityPlayer)player)) { }
                            }
                        }
                    }
                }
                else
                {
                    if (isAcceptedBlock(this.creature.world.getBlockState(this.destinationBlock).getBlock()))
                    {
                        this.creature.swingArm(EnumHand.MAIN_HAND);
                        this.creature.world.destroyBlock(this.destinationBlock, false);
                    }
                    this.actionDelay = 100;
                    state = 2;
                }
                break;
            case 2:
                scatterAway();
                if (--this.actionDelay < 0)
                {
                    destinationBlock = BlockPos.ORIGIN;
                    state = 0;
                }
                break;
            default:
                state = 0;
                break;
        }
    }

    /** The Shadow attempts to flee after breaking a Torch, to prevent the player from finding it immediently */
    public void scatterAway()
    {
        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom( this.creature, 10, 6, new Vec3d(this.destinationBlock));

        if (vec3d != null)
        { this.creature.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.scatterSpeed); }
    }

    /** Find a nearby Light-producing block that can be reached. */
    private boolean findViableLightTarget()
    {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(this.creature.getPosition());

        for (int radius = 1; radius <= this.searchLength; radius++)
        {
            for (int x1 = -radius; x1 <= radius; x1++)
            {
                for (int y1 = -radius; y1 <= radius; y1++)
                {
                    for (int z1 = -radius; z1 <= radius; z1++)
                    {
                        /* Zero need to re-check positions, so only scan at the edge of the current value of radius! */
                        if (Math.abs(x1) == radius || Math.abs(y1) == radius || Math.abs(z1) == radius) blockPos.setPos(this.creature.posX + x1, this.creature.posY + y1, this.creature.posZ + z1);

                        if (this.creature.isWithinHomeDistanceFromPosition(blockPos) && this.shouldMoveTo(this.creature.world, blockPos) && creature.getNavigator().getPathToPos(blockPos) != null)
                        {
                            this.destinationBlock = blockPos;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        if (isAcceptedBlock(worldIn.getBlockState(pos).getBlock()))
        {
            Block blockBelowType = worldIn.getBlockState(pos.down()).getBlock();

            if (blockBelowType.isTopSolid(worldIn.getBlockState(pos.down())) && !worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN))
            { return true; }
        }
        return false;
    }

    protected boolean isAcceptedBlock(Block block)
    { return block instanceof BlockTorch; }
}
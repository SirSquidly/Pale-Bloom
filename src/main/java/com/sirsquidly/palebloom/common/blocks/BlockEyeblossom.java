package com.sirsquidly.palebloom.common.blocks;

import com.sirsquidly.palebloom.config.Config;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockEyeblossom extends BlockBush implements IEyeblossomListener
{
    boolean isOpen;

    public BlockEyeblossom(boolean isOpenIn)
    {
        super(Material.PLANTS, isOpenIn ? MapColor.ADOBE : MapColor.GREEN_STAINED_HARDENED_CLAY);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        isOpen = isOpenIn;
        //setSoundType(BOMDSoundTypes.MOSS);
    }

    /** Occurs randomly, meaning this Eyeblossom is NOT responding. */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, false); }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, true); }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        int worldTime = (int)(worldIn.getWorldTime() % 24000L);
        boolean isNight = worldTime >= 12600 && worldTime <= 23400;

        IBlockState state = isNight ? JTPGBlocks.EYEBLOSSOM_OPEN.getDefaultState() : JTPGBlocks.EYEBLOSSOM_CLOSED.getDefaultState();
        /** Cancel any further logic if this Eyeblossom is already correct. */
        if (worldIn.getBlockState(pos).equals(state)) return;

        SoundEvent sound = isNight ? (isResponding ? JTPGSounds.BLOCK_EYEBLOSSOM_OPEN : JTPGSounds.BLOCK_EYEBLOSSOM_OPEN_LONG) : (isResponding ? JTPGSounds.BLOCK_EYEBLOSSOM_CLOSE : JTPGSounds.BLOCK_EYEBLOSSOM_CLOSE_LONG);

        worldIn.setBlockState(pos, state, 2);
        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);

        double hx = pos.getX() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
        double hy = pos.getY() + 2 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
        double hz = pos.getZ() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
        paleBloom.proxy.spawnParticle(0, worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, hx, hy, hz, !isNight ? 0 : 1);

        for (BlockPos nearbyPos : BlockPos.getAllInBoxMutable(pos.add(-3, -2, -3), pos.add(3, 2, 3)))
        {
            if (nearbyPos.equals(pos)) continue;

            Block block = worldIn.getBlockState(nearbyPos).getBlock();

            if (block instanceof IEyeblossomListener)
            {
                int distance = Math.abs(pos.getX() - nearbyPos.getX()) + Math.abs(pos.getY() - nearbyPos.getY()) + Math.abs(pos.getZ() - nearbyPos.getZ());

                int baseDelay = Math.max(6, (distance * distance * distance) / 3);
                int randomExtra = worldIn.rand.nextInt(Math.max(8, distance));

                worldIn.scheduleUpdate(nearbyPos, block, baseDelay + randomExtra);
            }
        }
    }


    /** Render texture at fullbright, if open, and the config option is true. */
    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    { return isOpen && Config.block.eyeblossomFullbright ? 15728880 : super.getPackedLightmapCoords(state, source, pos); }

    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        /* Only open Eyeblossoms do cool stuff!*/
        if (!isOpen) return;

        if (rand.nextInt(512) == 0 && worldIn.getBlockState(pos.down()).getBlock() == JTPGBlocks.PALE_MOSS)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, JTPGSounds.BLOCK_EYEBLOSSOM_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
    }
}
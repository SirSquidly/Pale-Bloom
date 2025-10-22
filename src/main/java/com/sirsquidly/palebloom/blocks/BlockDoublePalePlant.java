package com.sirsquidly.palebloom.blocks;

import com.sirsquidly.palebloom.Config;
import com.sirsquidly.palebloom.blocks.base.BlockJTPGDoublePlant;
import com.sirsquidly.palebloom.init.JTPGSounds;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDoublePalePlant extends BlockJTPGDoublePlant implements IEyeblossomListener
{
    public static final PropertyEnum<BlockDoublePalePlant.EnumType> TYPE = PropertyEnum.create("type", BlockDoublePalePlant.EnumType.class);
    public static final PropertyBool AWAKE = PropertyBool.create("awake");
    protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockDoublePalePlant()
    {
        this.setTickRandomly(true);

        setDefaultState(blockState.getBaseState().withProperty(AWAKE, false).withProperty(HALF, EnumBlockHalf.LOWER));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return BUSH_AABB; }

    /** Does the placeAt when placed. */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    { this.placeDoubleAt(worldIn, pos, Math.min(EnumType.META_LOOKUP.length, stack.getMetadata()), false, 2); }

    /** Places the double tall plant. */
    public void placeDoubleAt(World worldIn, BlockPos lowerPos, int type, boolean awake, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(AWAKE, awake).withProperty(HALF, EnumBlockHalf.LOWER).withProperty(TYPE, BlockDoublePalePlant.EnumType.byMetadata(type)), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(AWAKE, awake).withProperty(HALF, EnumBlockHalf.UPPER).withProperty(TYPE, BlockDoublePalePlant.EnumType.byMetadata(type)), flags);
    }

    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE).getMetadata();
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        for (int i = 0; i <= 2; i++)
        { items.add(new ItemStack(this, 1, i)); }
    }

    /** Controls the 'awake' value of the double plant. */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    { preformSwapping(worldIn, pos, false); }

    public void preformSwapping(World worldIn, BlockPos pos, boolean isResponding)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (worldIn.isRemote || state.getValue(HALF) != EnumBlockHalf.LOWER) return;

        boolean isNight = com.sirsquidly.palebloom.world.WorldPaleGarden.isNight(worldIn);

        /** If night and Awake match, there is no need to update anything. */
        if (state.getValue(AWAKE) == isNight) return;

        SoundEvent sound = isNight ? JTPGSounds.BLOCK_EYEBLOSSOM_OPEN_LONG : JTPGSounds.BLOCK_EYEBLOSSOM_CLOSE_LONG;

        worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
        this.placeDoubleAt(worldIn, pos, state.getValue(TYPE).meta, isNight, 2);

        if (state.getValue(TYPE) == EnumType.EYEBLOSSOM_BUSH)
        { spawnResinParticles(worldIn, pos, 10, isNight); }
    }

    /** Spawns a bunch of Resin Particles, to match the behavior of the Eyeblossom. */
    public void spawnResinParticles(World worldIn, BlockPos pos, int particleAmount, boolean isNight)
    {
        for (int i = 0; i < particleAmount; i ++)
        {
            double sX = pos.getX() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            double sY = pos.getY() + 1.25 + (worldIn.rand.nextDouble() * 0.75 - 0.5);
            double sZ = pos.getZ() + 0.5 +(worldIn.rand.nextDouble() * 0.5 - 0.25);

            double hx = pos.getX() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            double hy = sY + 1.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            double hz = pos.getZ() + 0.5 + (worldIn.rand.nextDouble() * 0.5 - 0.25);
            paleBloom.proxy.spawnParticle(0, worldIn, sX, sY, sZ, hx, hy, hz, !isNight ? 0 : 1);
        }
    }

    /** Fullbright only when it's an Awake Eyeblossom Bush. */
    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    { return state.getValue(TYPE) == EnumType.EYEBLOSSOM_BUSH && state.getValue(AWAKE) && Config.block.eyeblossomFullbright ? 15728880 : super.getPackedLightmapCoords(state, source, pos); }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumType type = EnumType.values()[meta & 3];
        EnumBlockHalf half = (meta & 4) == 0 ? EnumBlockHalf.LOWER : EnumBlockHalf.UPPER;
        boolean awake = (meta & 8) != 0;
        return this.getDefaultState().withProperty(TYPE, type).withProperty(HALF, half).withProperty(AWAKE, awake);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta |= state.getValue(TYPE).ordinal();
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) meta |= 4;
        if (state.getValue(AWAKE)) meta |= 8;
        return meta;
    }

    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, AWAKE, HALF, TYPE); }

    public static enum EnumType implements IStringSerializable
    {
        EYEBLOSSOM_BUSH(0, "eyeblossom_bush"),
        STIFFPOD(1, "stiffpod"),
        EPIALES(2, "epiales");

        private static final BlockDoublePalePlant.EnumType[] META_LOOKUP = new BlockDoublePalePlant.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String translationKey;

        private EnumType(int metaIn, String nameIn)
        { this(metaIn, nameIn, nameIn); }

        private EnumType(int metaIn, String nameIn, String unlocalizedNameIn)
        {
            this.meta = metaIn;
            this.name = nameIn;
            this.translationKey = unlocalizedNameIn;
        }

        public int getMetadata()
        { return this.meta; }

        public String toString()
        { return this.name; }

        public static BlockDoublePalePlant.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            { meta = 0; }
            return META_LOOKUP[meta];
        }

        public String getName()
        { return this.name; }

        public String getTranslationKey()
        { return this.translationKey; }

        static
        {
            for (BlockDoublePalePlant.EnumType type : values())
            { META_LOOKUP[type.getMetadata()] = type; }
        }
    }
}
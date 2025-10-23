package com.sirsquidly.palebloom.common.blocks.base;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockJTPGSlab extends BlockSlab
{
	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
	
	public BlockJTPGSlab(Material materialIn, SoundType soundIn, float hardnessIn, float resistenceIn)
    {
        super(materialIn);
        this.setSoundType(soundIn);
        this.useNeighborBrightness = true;
        this.setHardness(hardnessIn);
        this.setResistance(resistenceIn);
		this.setLightOpacity(255);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

		this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]); }

    @Override
    public int getMetaFromState(IBlockState state)
    { return state.getValue(HALF).ordinal() + 1; }

    @Override
    protected BlockStateContainer createBlockState()
	{ return new BlockStateContainer(this, VARIANT, HALF); }

	@Override
	public String getTranslationKey(int meta)
	{ return getTranslationKey(); }

	@Override
	public boolean isDouble()
	{
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty() { return VARIANT; }
	@Override
	public Comparable<?> getTypeForItem(ItemStack itemStack) { return Variant.DEFAULT;  }

	public static enum Variant implements IStringSerializable
	{
		DEFAULT;
		@Override
		public String getName() {  return "default"; }
	}
}
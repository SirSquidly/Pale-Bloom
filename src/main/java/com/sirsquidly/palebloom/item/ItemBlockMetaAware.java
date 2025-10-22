package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.blocks.BlockDoublePalePlant;
import com.sirsquidly.palebloom.blocks.base.BlockJTPGSapling;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
* It's like ItemBlock, but it ACTUALLY WORKS WITH METADATA, WOW!
*
 * Seriously, what a bother
* */
public class ItemBlockMetaAware extends ItemBlock
{
    public ItemBlockMetaAware(Block block)
    {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.hasSubtypes = true;
    }

    /** Override the name if it is a Sapling.
     *
     * In the future, if more metadata blocks require custom names, I'll just create an interface for it.
     * */
    public String getTranslationKey(ItemStack stack)
    {
        if (block instanceof BlockJTPGSapling)
        { return "tile." + paleBloom.MOD_ID + "." + BlockJTPGSapling.EnumType.byMetadata(stack.getMetadata()).getName() + "_sapling"; }

        if (block instanceof BlockDoublePalePlant)
        { return "tile." + paleBloom.MOD_ID + "." + BlockDoublePalePlant.EnumType.byMetadata(stack.getMetadata()).getName(); }

        return super.getTranslationKey(stack);
    }

    @Override
    public int getMetadata(int damage)
    { return damage; }
}

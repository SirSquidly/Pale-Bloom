package com.sirsquidly.palebloom.common.blocks.tileentity;

import com.sirsquidly.palebloom.common.world.WorldPaleGarden;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileMoonbrewbloom extends TileEntity implements ITickable
{
    WorldPaleGarden WorldPaleGarden = new WorldPaleGarden();

    @Override
    public void update()
    {
        if(!world.isRemote && world.getTotalWorldTime() % 60L == 0L)
        {
            WorldPaleGarden.requestBulbResin(world, pos, 1);
        }
    }
}

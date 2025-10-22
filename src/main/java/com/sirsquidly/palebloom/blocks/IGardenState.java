package com.sirsquidly.palebloom.blocks;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public interface IGardenState
{
    public static final PropertyEnum<EnumLucidityState> LUCIDITY_STATE = PropertyEnum.create("lucidity_state", EnumLucidityState.class);

    public static enum EnumLucidityState implements IStringSerializable
    {
        DORMANT("dormant"),
        LUCID("lucid"),
        AWAKE("awake");
        private final String name;

        private EnumLucidityState(String name)
        { this.name = name; }

        public String toString()
        { return this.name; }
        public String getName()
        { return this.name; }
    }
}
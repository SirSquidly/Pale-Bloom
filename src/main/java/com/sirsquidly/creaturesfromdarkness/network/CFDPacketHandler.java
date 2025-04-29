package com.sirsquidly.creaturesfromdarkness.network;

import com.sirsquidly.creaturesfromdarkness.creaturesfromdarkness;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CFDPacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(creaturesfromdarkness.MOD_ID);
	
	public static void registerMessages()
	{
		int messageId = 0;
		
		CHANNEL.registerMessage(CFDPacketNightmareNear.Handler.class, CFDPacketNightmareNear.class, messageId++, Side.CLIENT);
		//CHANNEL.registerMessage(OEPacketRiptide.Handler.class, OEPacketRiptide.class, messageId++, Side.CLIENT);
	}
}

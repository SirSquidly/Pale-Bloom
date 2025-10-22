package com.sirsquidly.palebloom.network;

import com.sirsquidly.palebloom.paleBloom;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class JTPGPacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(paleBloom.MOD_ID);
	
	public static void registerMessages()
	{
		int messageId = 0;

		CHANNEL.registerMessage(JTPGPacketSpawnParticles.Handler.class, JTPGPacketSpawnParticles.class, messageId++, Side.CLIENT);
		CHANNEL.registerMessage(JTPGPacketFogCapability.Handler.class, JTPGPacketFogCapability.class, messageId++, Side.CLIENT);
	}
}

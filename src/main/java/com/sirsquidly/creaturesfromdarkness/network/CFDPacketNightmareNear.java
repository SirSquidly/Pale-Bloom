package com.sirsquidly.creaturesfromdarkness.network;

import com.sirsquidly.creaturesfromdarkness.capabilities.CapabilityNightmare;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** This is used so the Server can tell Clients when the Riptide Capability is altered, so animations are displayed. */
public class CFDPacketNightmareNear implements IMessage
{
	private int entityId;
	private int nearbyInt;

	public CFDPacketNightmareNear() {}

	public CFDPacketNightmareNear(int entityId, int nearInt)
	{
		this.entityId = entityId;
		this.nearbyInt = nearInt;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = ByteBufUtils.readVarInt(buf, 5);
		nearbyInt = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, entityId, 5);
		ByteBufUtils.writeVarInt(buf, nearbyInt, 5);
	}

	public static class Handler implements IMessageHandler<CFDPacketNightmareNear, IMessage>
	{
		@Override
		public IMessage onMessage(CFDPacketNightmareNear message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
					{
						Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

						if(player.hasCapability(CapabilityNightmare.RIPTIDE_CAP, null))
						{
							player.getCapability(CapabilityNightmare.RIPTIDE_CAP, null).setNightmareNearby(message.nearbyInt);
						}
					}
			);
			return null;
		}
	}
}
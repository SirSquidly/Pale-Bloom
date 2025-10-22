package com.sirsquidly.palebloom.network;

import com.sirsquidly.palebloom.capabilities.CapabilityPaleGardenFog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** This is used so the Server can tell Clients when the Riptide Capability is altered, so animations are displayed. */
public class JTPGPacketFogCapability implements IMessage
{
	private int entityId;
	private boolean enableFog;

	public JTPGPacketFogCapability() {}

	public JTPGPacketFogCapability(int entityId, boolean enableFogIn)
	{
		this.entityId = entityId;
		this.enableFog = enableFogIn;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = ByteBufUtils.readVarInt(buf, 5);
		enableFog = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, entityId, 5);
		buf.writeBoolean(enableFog);
	}

	public static class Handler implements IMessageHandler<JTPGPacketFogCapability, IMessage>
	{
		@Override
		public IMessage onMessage(JTPGPacketFogCapability message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
					{
						Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

						if(player.hasCapability(CapabilityPaleGardenFog.PALE_GARDEN_FOG_CAP, null))
						{
							player.getCapability(CapabilityPaleGardenFog.PALE_GARDEN_FOG_CAP, null).setPlayerFogEnabled(message.enableFog);
						}
					}
			);
			return null;
		}
	}
}
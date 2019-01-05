package bl4ckscor3.mod.globalxp.network.packets;

import bl4ckscor3.mod.globalxp.GlobalXP;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.CustomPayloadClientPacket;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketHelper 
{
	public static CustomPayloadClientPacket createUpdateXPPacket(BlockPos pos, int storedXP) 
	{
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeLong(pos.asLong());
		buf.writeInt(storedXP);
		
		return new CustomPayloadClientPacket(GlobalXP.UPDATE_XP_BLOCK, buf);
	}
}

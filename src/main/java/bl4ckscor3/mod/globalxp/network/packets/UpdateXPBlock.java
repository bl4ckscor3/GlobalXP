package bl4ckscor3.mod.globalxp.network.packets;

import java.util.function.Supplier;

import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class UpdateXPBlock
{
	private BlockPos pos;
	private int storedXP;

	public UpdateXPBlock() {}

	/**
	 * Initializes this packet with a tile entity
	 * @param te The tile entity to initialize with
	 */
	public UpdateXPBlock(TileEntityXPBlock te)
	{
		this(te.getPos(), te.getStoredXP());
	}

	/**
	 * Initializes this packet
	 * @param p The position of the tile entity
	 * @param sL The amount of XP stored in it
	 */
	public UpdateXPBlock(BlockPos p, int sX)
	{
		pos = p;
		storedXP = sX;
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
		buf.writeInt(storedXP);
	}

	public void fromBytes(ByteBuf buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		storedXP = buf.readInt();
	}

	public static void encode(UpdateXPBlock message, PacketBuffer packet)
	{
		message.toBytes(packet);
	}

	public static UpdateXPBlock decode(PacketBuffer packet)
	{
		UpdateXPBlock message = new UpdateXPBlock();

		message.fromBytes(packet);
		return message;
	}

	public static void onMessage(UpdateXPBlock message, Supplier<NetworkEvent.Context> ctx)
	{
		Minecraft.getInstance().addScheduledTask(() -> {
			if(Minecraft.getInstance().world.getTileEntity(message.pos) instanceof TileEntityXPBlock)
				((TileEntityXPBlock)Minecraft.getInstance().world.getTileEntity(message.pos)).setStoredXP(message.storedXP);

			ctx.get().setPacketHandled(true);
		});
	}
}

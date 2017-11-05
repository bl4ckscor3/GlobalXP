package bl4ckscor3.mod.globalxp.network.packets;

import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketUpdateXPBlock implements IMessage
{
	private BlockPos pos;
	private int storedLevels;

	public SPacketUpdateXPBlock() {}
	
	/**
	 * Initializes this packet with a tile entity
	 * @param te The tile entity to initialize with
	 */
	public SPacketUpdateXPBlock(TileEntityXPBlock te)
	{
		this(te.getPos(), te.getStoredLevels());
	}
	
	/**
	 * Initializes this packet
	 * @param p The position of the tile entity
	 * @param sL The amount of stored levels in it
	 */
	public SPacketUpdateXPBlock(BlockPos p, int sL)
	{
		pos = p;
		storedLevels = sL;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
		buf.writeInt(storedLevels);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		storedLevels = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<SPacketUpdateXPBlock, IMessage>
	{
		@Override
		public IMessage onMessage(SPacketUpdateXPBlock message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if(Minecraft.getMinecraft().world.getTileEntity(message.pos) != null)
					((TileEntityXPBlock)Minecraft.getMinecraft().world.getTileEntity(message.pos)).setStoredLevels(message.storedLevels);
			});
			return null;
		}
	}
}

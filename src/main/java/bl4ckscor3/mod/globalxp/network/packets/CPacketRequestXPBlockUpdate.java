package bl4ckscor3.mod.globalxp.network.packets;

import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketRequestXPBlockUpdate implements IMessage
{
	private BlockPos pos;
	private int dimension;
	
	public CPacketRequestXPBlockUpdate() {}
	
	/**
	 * Initializes this packet with a tile entity
	 * @param te The tile entity to initialize with
	 */
	public CPacketRequestXPBlockUpdate(TileEntityXPBlock te)
	{
		this(te.getPos(), te.getWorld().provider.getDimension());
	}
	
	/**
	 * Initializes this packet
	 * @param p The position of the tile entity
	 * @param dim The dimension it is in
	 */
	public CPacketRequestXPBlockUpdate(BlockPos p, int dim)
	{
		pos = p;
		dimension = dim;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<CPacketRequestXPBlockUpdate, SPacketUpdateXPBlock>
	{
		@Override
		public SPacketUpdateXPBlock onMessage(CPacketRequestXPBlockUpdate message, MessageContext ctx)
		{
			TileEntityXPBlock te = (TileEntityXPBlock)FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension).getTileEntity(message.pos);
			
			if(te != null)
				return new SPacketUpdateXPBlock(te);
			else
				return null;
		}
	}
}

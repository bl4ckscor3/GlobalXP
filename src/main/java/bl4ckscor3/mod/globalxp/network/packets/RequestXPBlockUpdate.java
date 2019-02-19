package bl4ckscor3.mod.globalxp.network.packets;

import java.util.function.Supplier;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RequestXPBlockUpdate
{
	private BlockPos pos;
	private int dimension;

	public RequestXPBlockUpdate() {}

	/**
	 * Initializes this packet with a tile entity
	 * @param te The tile entity to initialize with
	 */
	public RequestXPBlockUpdate(TileEntityXPBlock te)
	{
		this(te.getPos(), te.getWorld().getWorldInfo().getDimension());
	}

	/**
	 * Initializes this packet
	 * @param p The position of the tile entity
	 * @param dim The dimension it is in
	 */
	public RequestXPBlockUpdate(BlockPos p, int dim)
	{
		pos = p;
		dimension = dim;
	}

	public void toBytes(PacketBuffer buf)
	{
		buf.writeLong(pos.toLong());
		buf.writeInt(dimension);
	}

	public void fromBytes(PacketBuffer buf)
	{
		pos = BlockPos.fromLong(buf.readLong());
		dimension = buf.readInt();
	}

	public static void encode(RequestXPBlockUpdate message, PacketBuffer packet)
	{
		message.toBytes(packet);
	}

	public static RequestXPBlockUpdate decode(PacketBuffer packet)
	{
		RequestXPBlockUpdate message = new RequestXPBlockUpdate();

		message.fromBytes(packet);
		return message;
	}

	public static void onMessage(RequestXPBlockUpdate message, Supplier<NetworkEvent.Context> ctx)
	{
		TileEntity te = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), DimensionType.getById(message.dimension), false, false).getTileEntity(message.pos);

		if(te instanceof TileEntityXPBlock)
			GlobalXP.channel.reply(new UpdateXPBlock((TileEntityXPBlock)te), ctx.get());

		ctx.get().setPacketHandled(true);
	}
}

package bl4ckscor3.mod.globalxp.imc.waila;

import java.util.List;

import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WailaDataProvider implements IWailaDataProvider
{
	public static void callbackRegister(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new WailaDataProvider(), XPBlock.class);
		registrar.registerStackProvider(new WailaDataProvider(), XPBlock.class);
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity arg1, NBTTagCompound arg2, World arg3, BlockPos arg4)
	{
		return arg2;
	}

	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3)
	{
		if(!(arg2.getTileEntity() instanceof TileEntityXPBlock))
			return arg1;

		TileEntityXPBlock te = ((TileEntityXPBlock)arg2.getTileEntity());

		arg1.add(I18n.format("info.levels", String.format("%.2f", te.getStoredLevels())));

		if(arg2.getPlayer().isSneaking())
			arg1.add(I18n.format("info.xp", te.getStoredXP()));

		return arg1;
	}

	@Override
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3)
	{
		return arg1;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor arg0, IWailaConfigHandler arg1)
	{
		if(!(arg0.getBlock() instanceof XPBlock))
			return new ItemStack(Blocks.AIR);
		return ((XPBlock)(arg0.getBlock())).getWailaDisplayStack();
	}

	@Override
	public List<String> getWailaTail(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2, IWailaConfigHandler arg3)
	{
		return arg1;
	}
}

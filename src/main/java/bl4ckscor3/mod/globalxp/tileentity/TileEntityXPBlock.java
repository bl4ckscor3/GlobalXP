package bl4ckscor3.mod.globalxp.tileentity;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.network.packets.CPacketRequestXPBlockUpdate;
import bl4ckscor3.mod.globalxp.network.packets.SPacketUpdateXPBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityXPBlock extends TileEntity
{
	private int storedLevels = 0;
	
	/**
	 * Adds one level to this tile entity and updates all clients within a 64 block range with that change
	 */
	public void addLevel()
	{
		addLevel(1);
	}
	
	/**
	 * Adds levels to this tile entity and updates all clients within a 64 block range with that change
	 * @param lvl The amount of levels to add
	 */
	public void addLevel(int lvl)
	{
		storedLevels += lvl;
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
	}
	
	/**
	 * Removes one level from the storage if it is over 0 and updates all clients within a 64 block range with that change
	 */
	public void removeLevel()
	{
		if(storedLevels - 1 < 0)
			return;
		
		storedLevels -= 1;
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
	}
	
	/**
	 * Sets how many levels are stored in this tile entity
	 * @param levels The amount of levels, non negative
	 */
	public void setStoredLevels(int levels)
	{
		storedLevels = levels;
	}
	
	/**
	 * Gets how many XP are stored in this tile entity
	 * @return The total amount of XP stored in this tile entity
	 */
	public int getStoredLevels()
	{
		return storedLevels;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("stored_levels", storedLevels);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		storedLevels = tag.getInteger("stored_levels");
		super.readFromNBT(tag);
	}
	
	@Override
	public void onLoad()
	{
		if(world.isRemote)
			GlobalXP.network.sendToServer(new CPacketRequestXPBlockUpdate(this));
	}
}

package bl4ckscor3.mod.globalxp.tileentity;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.network.packets.CPacketRequestXPBlockUpdate;
import bl4ckscor3.mod.globalxp.network.packets.SPacketUpdateXPBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class TileEntityXPBlock extends TileEntity
{
	private int storedXP = 0;
	private float storedLevels = 0f;
	
	/**
	 * Adds xp to this tile entity and updates all clients within a 64 block range with that change
	 * @param amount The amount of xp to add
	 */
	public void addXP(int amount)
	{
		storedXP += amount;
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
		calculateStoredLevels();
	}
	
	/**
	 * Removes xp from the storage and returns amount removed.	Updates all clients within a 64 block range with that change
	 * @param amount The amount of xp to remove
	 */
	public int removeXP(int amount)
	{
		int amountRemoved = Math.min(amount, storedXP);

		if(amountRemoved <= 0)
		{
			return 0;
		}

		storedXP -= amountRemoved;
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
		calculateStoredLevels();
		return amountRemoved;
	}
	
	/**
	 * Sets how much XP is stored in this tile entity
	 * @param xp The amount of xp
	 */
	public void setStoredXP(int xp)
	{
		storedXP = xp;
		calculateStoredLevels();
	}
	
	/**
	 * Gets how many XP are stored in this tile entity
	 * @return The total amount of XP stored in this tile entity
	 */
	public int getStoredXP()
	{
		return storedXP;
	}

	/**
	 * Gets how many levels are stored.
	 * This value is only used for display purposes and does not reflect partial levels
	*/
	public float getStoredLevels()
	{
		return storedLevels;
	}

	/**
	 * Calculates total levels based on stored XP.
	*/
	protected void calculateStoredLevels()
	{
		storedLevels = 0f;

		int xp = storedXP;

		while(xp > 0)
		{
			int xpToNextLevel = getXPForLevel((int)storedLevels);
			if(xp < xpToNextLevel)
			{
				storedLevels += (float)xp / xpToNextLevel;
				break;
			}
			xp -= xpToNextLevel;
			storedLevels += 1.0f;
		}
	}
	
	/**
	 * Gets XP requirement to go from level to level+1
	 * Formula copied from EntityPlayer.java
	 * @param level The level to get XP for
	 * @return The amount of XP required to reach the next level
	 */
	private int getXPForLevel(int level)
	{
		if(level >= 30)
			return 112 + (level - 30) * 9;
		return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("stored_xp", storedXP);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		setStoredXP(tag.getInteger("stored_xp"));
		super.readFromNBT(tag);
	}
	
	@Override
	public void onLoad()
	{
		if(world.isRemote)
			GlobalXP.network.sendToServer(new CPacketRequestXPBlockUpdate(this));
	}
}

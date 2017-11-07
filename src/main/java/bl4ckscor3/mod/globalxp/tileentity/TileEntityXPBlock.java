package bl4ckscor3.mod.globalxp.tileentity;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.network.packets.CPacketRequestXPBlockUpdate;
import bl4ckscor3.mod.globalxp.network.packets.SPacketUpdateXPBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class TileEntityXPBlock extends TileEntity
{

	private int storedXp = 0;
	private float storedLevels = 0f;
	
	/**
	 * Adds xp to this tile entity and updates all clients within a 64 block range with that change
	 * @param lvl The amount of xp to add
	 */
	public void addXp(int amount)
	{
		storedXp += amount;
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
		calculateStoredLevels();
	}
	
	/**
	 * Removes xp from the storage and returns amount removed.	Updates all clients within a 64 block range with that change
	 */
	public int removeXp(int amount)
	{
		int amountRemoved = Math.min(amount, storedXp);

		if(amountRemoved <= 0) {
			return 0;
		}

		storedXp -= amountRemoved;
		
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));

		calculateStoredLevels();
		return amountRemoved;
	}
	
	/**
	 * Sets how much XP is stored in this tile entity
	 * @param xp The amount of xp
	 */
	public void setStoredXp(int xp)
	{
		storedXp = xp;
		calculateStoredLevels();
	}
	
	/**
	 * Gets how many XP are stored in this tile entity
	 * @return The total amount of XP stored in this tile entity
	 */
	public int getStoredXp()
	{
		return storedXp;
	}

	/**
	 * Gets how many levels are stored.
	 * This value is only used for display purposes and does not reflect partial levels
	*/
	public float getStoredLevels() {
		return storedLevels;
	}

	protected void calculateStoredLevels() {
		storedLevels = 0f;

		int xp = storedXp;
		while (xp > 0) {
			int xpToNextLevel = getXpForLevel((int)storedLevels);
			if (xp < xpToNextLevel) {
				storedLevels += (float)xp / xpToNextLevel;
				break;
			}
			xp -= xpToNextLevel;
			storedLevels += 1.0f;
		}
	}
	
	// Gets xp requirement to go from level to level+1
	// formula copied from EntityPlayer.java
	private int getXpForLevel(int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		}
		return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("stored_xp", storedXp);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		setStoredXp(tag.getInteger("stored_xp"));
		super.readFromNBT(tag);
	}
	
	@Override
	public void onLoad()
	{
		if(world.isRemote)
			GlobalXP.network.sendToServer(new CPacketRequestXPBlockUpdate(this));
	}
}

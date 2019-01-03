package bl4ckscor3.mod.globalxp.blockentities;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.utils.XPUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class XPBlockEntity extends BlockEntity {
	
	private int storedXP = 0;
	private float storedLevels = 0.0F;

	public XPBlockEntity() {
		super(GlobalXP.BLOCK_ENTITY_OWNABLE);
	}
	
	/**
	 * Adds XP to this tile entity and updates all clients within a 64 block range with that change
	 * @param amount The amount of XP to add
	 */
	public void addXP(int amount)
	{
		storedXP += amount;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
	}

	/**
	 * Removes XP from the storage and returns amount removed.
	 * Updates all clients within a 64 block range with that change
	 * @param amount The amount of XP to remove
	 * @return The amount of XP that has been removed
	 */
	public int removeXP(int amount)
	{
		int amountRemoved = Math.min(amount, storedXP);

		if(amountRemoved <= 0)
			return 0;

		storedXP -= amountRemoved;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		markDirty();
		GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock(this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
		return amountRemoved;
	}

	/**
	 * Sets how much XP is stored in this tile entity
	 * @param xp The amount of XP
	 */
	public void setStoredXP(int xp)
	{
		storedXP = xp;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
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
	 * @return The amount of levels stored
	 */
	public float getStoredLevels()
	{
		return storedLevels;
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		tag.putInt("stored_xp", storedXP);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		setStoredXP(tag.getInt("stored_xp"));
		super.fromTag(tag);
	}

}

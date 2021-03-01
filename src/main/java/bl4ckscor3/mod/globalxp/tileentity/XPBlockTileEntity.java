package bl4ckscor3.mod.globalxp.tileentity;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.util.XPUtils;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;

public class XPBlockTileEntity extends TileEntity implements ITickableTileEntity
{
	private int storedXP = 0;
	private float storedLevels = 0.0F;
	private boolean destroyedByCreativePlayer;

	public XPBlockTileEntity()
	{
		super(GlobalXP.teTypeXpBlock);
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
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
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
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		return amountRemoved;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(pkt.getNbtCompound());
	}

	/**
	 * Sets how much XP is stored in this tile entity
	 * @param xp The amount of XP
	 */
	public void setStoredXP(int xp)
	{
		storedXP = xp;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		markDirty();

		if(world != null)
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
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

	/**
	 * Sets whether the corresponding block will be destroyed by a creative player. Used to determine drops
	 */
	public void setDestroyedByCreativePlayer(boolean destroyedByCreativePlayer)
	{
		this.destroyedByCreativePlayer = destroyedByCreativePlayer;
	}

	/**
	 * @return true if the corresponding block was destroyed by a creative player, false otherwhise
	 */
	public boolean isDestroyedByCreativePlayer()
	{
		return destroyedByCreativePlayer;
	}

	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		tag.putInt("stored_xp", storedXP);
		return super.write(tag);
	}

	@Override
	public void read(CompoundNBT tag)
	{
		setStoredXP(tag.getInt("stored_xp"));
		super.read(tag);
	}

	@Override
	public void tick()
	{
		if(world.isRemote || !Configuration.SERVER.pickupXP.get())
			return;

		if(world.getGameTime() % 5 == 0)
			pickupDroppedXP();
	}

	private void pickupDroppedXP()
	{
		for(ExperienceOrbEntity entity : world.<ExperienceOrbEntity>getEntitiesWithinAABB(ExperienceOrbEntity.class, getPickupArea(), EntityPredicates.IS_ALIVE))
		{
			int amount = entity.getXpValue();

			if(entity.isAlive() && getStoredXP() + amount <= getCapacity())
			{
				addXP(amount);
				entity.remove();
			}
		}
	}

	/**
	 * @return The area around the tile entity to search for xp orbs.
	 */
	private AxisAlignedBB getPickupArea()
	{
		double x = getPos().getX() + 0.5D;
		double y = getPos().getY() + 0.5D;
		double z = getPos().getZ() + 0.5D;
		double range = Configuration.SERVER.pickupRange.get() + 0.5D;

		return new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
	}

	/**
	 * Gets the total amount of XP that can be stored in this tile entity
	 * @return The total amount of XP that can be stored in this tile entity
	 */
	public int getCapacity()
	{
		return Integer.MAX_VALUE;
	}
}

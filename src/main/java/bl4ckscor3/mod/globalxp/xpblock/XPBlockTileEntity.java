package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.block.BlockState;
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
		setChanged();
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
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
		setChanged();
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
		return amountRemoved;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return save(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(getBlockState(), pkt.getTag());
	}

	/**
	 * Sets how much XP is stored in this tile entity
	 * @param xp The amount of XP
	 */
	public void setStoredXP(int xp)
	{
		storedXP = xp;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		setChanged();

		if(level != null)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
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
	public CompoundNBT save(CompoundNBT tag)
	{
		tag.putInt("stored_xp", storedXP);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag)
	{
		setStoredXP(tag.getInt("stored_xp"));
		super.load(state, tag);
	}

	@Override
	public void tick()
	{
		if(!level.isClientSide && level.getGameTime() % 5 == 0 && Configuration.SERVER.pickupXP.get() && !getBlockState().getValue(XPBlock.POWERED))
			pickupDroppedXP();
	}

	private void pickupDroppedXP()
	{
		//find all orbs in the area around the block, and ignore xp orbs that were spawned as a result of a player removing xp from the block
		for(ExperienceOrbEntity entity : level.<ExperienceOrbEntity>getEntitiesOfClass(ExperienceOrbEntity.class, getPickupArea(), EntityPredicates.ENTITY_STILL_ALIVE.and(e -> !e.getPersistentData().getBoolean("GlobalXPMarker"))))
		{
			int amount = entity.getValue();

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
		double x = getBlockPos().getX() + 0.5D;
		double y = getBlockPos().getY() + 0.5D;
		double z = getBlockPos().getZ() + 0.5D;
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

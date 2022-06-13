package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class XPBlockEntity extends BlockEntity
{
	private int storedXP = 0;
	private float storedLevels = 0.0F;
	private boolean destroyedByCreativePlayer;

	public XPBlockEntity(BlockPos pos, BlockState state)
	{
		super(GlobalXP.XP_BLOCK_ENTITY_TYPE.get(), pos, state);
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
	public CompoundTag getUpdateTag()
	{
		return saveWithoutMetadata();
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		load(pkt.getTag());
	}

	/**
	 * Sets how much XP is stored in this block entity
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
	 * Gets how many XP are stored in this block entity
	 * @return The total amount of XP stored in this block entity
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
	public void saveAdditional(CompoundTag tag)
	{
		tag.putInt("stored_xp", storedXP);
	}

	@Override
	public void load(CompoundTag tag)
	{
		setStoredXP(tag.getInt("stored_xp"));
		super.load(tag);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, XPBlockEntity be)
	{
		if(level.getGameTime() % 5 == 0 && Configuration.SERVER.pickupXP.get() && !state.getValue(XPBlock.POWERED))
			be.pickupDroppedXP();
	}

	private void pickupDroppedXP()
	{
		//find all orbs in the area around the block, and ignore xp orbs that were spawned as a result of a player removing xp from the block
		for(ExperienceOrb entity : level.getEntitiesOfClass(ExperienceOrb.class, getPickupArea(), EntitySelector.ENTITY_STILL_ALIVE.and(e -> !e.getPersistentData().getBoolean("GlobalXPMarker"))))
		{
			int amount = entity.getValue();

			if(getStoredXP() + amount <= getCapacity())
			{
				addXP(amount);
				entity.discard();
			}
		}
	}

	/**
	 * @return The area around the block entity to search for xp orbs.
	 */
	private AABB getPickupArea()
	{
		double x = getBlockPos().getX() + 0.5D;
		double y = getBlockPos().getY() + 0.5D;
		double z = getBlockPos().getZ() + 0.5D;
		double range = Configuration.SERVER.pickupRange.get() + 0.5D;

		return new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
	}

	/**
	 * Gets the total amount of XP that can be stored in this block entity
	 * @return The total amount of XP that can be stored in this block entity
	 */
	public int getCapacity()
	{
		return Integer.MAX_VALUE;
	}
}

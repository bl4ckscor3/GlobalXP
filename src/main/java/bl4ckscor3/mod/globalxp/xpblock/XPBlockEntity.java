package bl4ckscor3.mod.globalxp.xpblock;

import com.google.common.math.IntMath;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class XPBlockEntity extends BlockEntity implements Nameable {
	private Component name;
	private int storedXP = 0;
	private float storedLevels = 0.0F;
	private boolean destroyedByCreativePlayer;

	public XPBlockEntity(BlockPos pos, BlockState state) {
		super(GlobalXP.XP_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	/**
	 * Adds XP to this tile entity and updates all clients within a 64 block range with that change
	 *
	 * @param amount The amount of XP to add
	 * @return The amount of XP that was not added
	 */
	public int addXP(int amount) {
		int space = Integer.MAX_VALUE - storedXP;

		storedXP = IntMath.saturatedAdd(storedXP, amount);
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);

		if (amount > space)
			return amount - space;
		else
			return 0;
	}

	/**
	 * Removes XP from the storage and returns amount removed. Updates all clients within a 64 block range with that change
	 *
	 * @param amount The amount of XP to remove
	 * @return The amount of XP that has been removed
	 */
	public int removeXP(int amount) {
		int amountRemoved = Math.min(amount, storedXP);

		if (amountRemoved <= 0)
			return 0;

		storedXP -= amountRemoved;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		return amountRemoved;
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return saveCustomOnly(lookupProvider);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/**
	 * Sets how much XP is stored in this block entity
	 *
	 * @param xp The amount of XP
	 */
	public void setStoredXP(int xp) {
		storedXP = xp;
		storedLevels = XPUtils.calculateStoredLevels(storedXP);
		setChanged();

		if (level != null)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
	}

	/**
	 * Gets how many XP are stored in this block entity
	 *
	 * @return The total amount of XP stored in this block entity
	 */
	public int getStoredXP() {
		return storedXP;
	}

	/**
	 * Gets how many levels are stored. This value is only used for display purposes and does not reflect partial levels
	 *
	 * @return The amount of levels stored
	 */
	public float getStoredLevels() {
		return storedLevels;
	}

	/**
	 * Sets whether the corresponding block will be destroyed by a creative player. Used to determine drops
	 */
	public void setDestroyedByCreativePlayer(boolean destroyedByCreativePlayer) {
		this.destroyedByCreativePlayer = destroyedByCreativePlayer;
	}

	/**
	 * @return true if the corresponding block was destroyed by a creative player, false otherwhise
	 */
	public boolean isDestroyedByCreativePlayer() {
		return destroyedByCreativePlayer;
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		tag.putInt("stored_xp", storedXP);

		if (name != null)
			tag.putString("CustomName", Component.Serializer.toJson(name, lookupProvider));
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		super.loadAdditional(tag, lookupProvider);
		setStoredXP(tag.getIntOr("stored_xp", 0));
		name = parseCustomNameSafe(tag.get("CustomName"), lookupProvider);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter getter) {
		super.applyImplicitComponents(getter);
		name = getter.get(DataComponents.CUSTOM_NAME);
		setStoredXP(getter.get(GlobalXP.STORED_XP));
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CustomName");
		tag.remove("stored_xp");
	}

	@Override
	protected void collectImplicitComponents(Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.CUSTOM_NAME, name);
		builder.set(GlobalXP.STORED_XP, storedXP);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, XPBlockEntity be) {
		if (level.getGameTime() % 5 == 0 && Configuration.SERVER.pickupXP.get() && !state.getValue(XPBlock.POWERED))
			be.pickupDroppedXP();
	}

	private void pickupDroppedXP() {
		//find all orbs in the area around the block, and ignore xp orbs that were spawned as a result of a player removing xp from the block
		for (ExperienceOrb orb : level.getEntitiesOfClass(ExperienceOrb.class, getPickupArea(), EntitySelector.ENTITY_STILL_ALIVE.and(e -> !e.getPersistentData().getBoolean("GlobalXPMarker").orElse(false)))) {
			int amount = orb.getValue();

			if (getStoredXP() + amount <= getCapacity()) {
				int unused = addXP(amount);

				orb.discard();

				if (unused > 0)
					level.addFreshEntity(new ExperienceOrb(level, orb.getX(), orb.getY(), orb.getZ(), unused));
			}
		}
	}

	/**
	 * @return The area around the block entity to search for xp orbs.
	 */
	private AABB getPickupArea() {
		double x = getBlockPos().getX() + 0.5D;
		double y = getBlockPos().getY() + 0.5D;
		double z = getBlockPos().getZ() + 0.5D;
		double range = Configuration.SERVER.pickupRange.get() + 0.5D;

		return new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
	}

	/**
	 * Gets the total amount of XP that can be stored in this block entity
	 *
	 * @return The total amount of XP that can be stored in this block entity
	 */
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

	public void setCustomName(Component name) {
		this.name = name;
	}

	@Override
	public Component getName() {
		return name != null ? name : getDefaultName();
	}

	@Override
	public Component getDisplayName() {
		return getName();
	}

	@Override
	public Component getCustomName() {
		return name;
	}

	public Component getDefaultName() {
		return Component.translatable(GlobalXP.XP_BLOCK.get().getDescriptionId());
	}
}

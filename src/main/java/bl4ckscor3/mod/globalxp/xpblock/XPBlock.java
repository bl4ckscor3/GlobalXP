package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.openmods.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class XPBlock extends BaseEntityBlock {
	private static final VoxelShape SHAPE = Block.box(0.0001D, 0.0001D, 0.0001D, 15.999D, 15.999D, 15.999D);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public XPBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof XPBlockEntity xpBlock) {
			if (!level.isClientSide) {
				if (player.isShiftKeyDown()) {
					long xpToStore = 0;

					if (GlobalXP.CONFIG.storingAmount != -1)
						xpToStore = Math.min(GlobalXP.CONFIG.storingAmount, EnchantmentUtils.getPlayerXP(player));
					else if (GlobalXP.CONFIG.storeUntilPreviousLevel) {
						int xpForCurrentLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);

						xpToStore = EnchantmentUtils.getPlayerXP(player) - xpForCurrentLevel;

						if (xpToStore == 0 && player.experienceLevel > 0) //player has exactly x > 0 levels (xp bar looks empty)
							xpToStore = xpForCurrentLevel - EnchantmentUtils.getExperienceForLevel(player.experienceLevel - 1);
					}
					else
						xpToStore = EnchantmentUtils.getPlayerXP(player);

					if (xpToStore == 0)
						return InteractionResult.PASS;

					xpBlock.addXP(xpToStore); //store as much XP as possible
					EnchantmentUtils.addPlayerXP(player, -xpToStore); //negative value removes xp
					return InteractionResult.SUCCESS;
				}
				else if (!player.isShiftKeyDown()) {
					long xpRetrieved;

					if (GlobalXP.CONFIG.retrievalAmount != -1)
						xpRetrieved = (long) (xpBlock.removeXP(GlobalXP.CONFIG.retrievalAmount) * GlobalXP.CONFIG.retrievalPercentage);
					else if (GlobalXP.CONFIG.retriveUntilNextLevel) {
						long xpToRetrieve = EnchantmentUtils.getExperienceForLevel(player.experienceLevel + 1) - EnchantmentUtils.getPlayerXP(player);

						xpRetrieved = (long) (xpBlock.removeXP(xpToRetrieve) * GlobalXP.CONFIG.retrievalPercentage);
					}
					else {
						xpRetrieved = (long) (xpBlock.getStoredXP() * GlobalXP.CONFIG.retrievalPercentage);
						xpBlock.setStoredXP(0);
					}

					if (xpRetrieved > 0)
						addOrSpawnXPForPlayer(player, xpRetrieved);
				}
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private void addOrSpawnXPForPlayer(Player player, long amount) {
		if (GlobalXP.CONFIG.retrieveXPOrbs) {
			Level level = player.level();

			if (!level.isClientSide) {
				ExperienceOrb orb = new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), amount);

				orb.addTag("GlobalXPMarker"); //so the xp block won't pick it back up
				level.addFreshEntity(orb);
			}
		}
		else
			EnchantmentUtils.addPlayerXP(player, amount);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof XPBlockEntity xpBlock)
			return Math.min(15, Math.floorDiv(xpBlock.getStoredXP(), GlobalXP.CONFIG.xpForComparator));
		else
			return 0;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (level.isClientSide || !stack.hasTag())
			return;

		if (level.getBlockEntity(pos) instanceof XPBlockEntity xpBlock) {
			if (stack.hasCustomHoverName())
				xpBlock.setCustomName(stack.getHoverName());

			if (stack.hasTag()) {
				CompoundTag stackTag = stack.getTag();

				if (stackTag.contains("BlockEntityTag"))
					stackTag = stackTag.getCompound("BlockEntityTag");

				xpBlock.setStoredXP(stackTag.getInt("stored_xp"));
			}
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (level.getBlockEntity(pos) instanceof XPBlockEntity xpBlock)
			xpBlock.setDestroyedByCreativePlayer(player.isCreative());

		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock())
			return;

		if (level.getBlockEntity(pos) instanceof XPBlockEntity xpBlock) {
			ItemStack stack = new ItemStack(asItem());

			if (xpBlock.hasCustomName())
				stack.setHoverName(xpBlock.getCustomName());

			if (xpBlock.getStoredLevels() != 0) {
				CompoundTag stackTag = stack.getOrCreateTag();

				stackTag.putLong("stored_xp", xpBlock.getStoredXP());
				popResource(level, pos, stack);
			}
			else if (!xpBlock.isDestroyedByCreativePlayer())
				popResource(level, pos, stack);
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, level, pos, block, fromPos, isMoving);
		level.setBlockAndUpdate(pos, state.setValue(POWERED, level.hasNeighborSignal(pos)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new XPBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide ? null : createTickerHelper(type, GlobalXP.XP_BLOCK_ENTITY_TYPE, XPBlockEntity::serverTick);
	}
}

package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import openmods.utils.EnchantmentUtils;

public class XPBlock extends BaseEntityBlock
{
	private static final VoxelShape SHAPE = Block.box(0.0001D, 0.0001D, 0.0001D, 15.999D, 15.999D, 15.999D);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public XPBlock()
	{
		super(Block.Properties.of(Material.METAL).strength(12.5F, 2000.0F).sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		BlockEntity tile = level.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity te)
		{
			if(!level.isClientSide)
			{
				if(player.isShiftKeyDown())
				{
					int xpToStore = 0;

					if(Configuration.SERVER.storingAmount.get() != -1)
						xpToStore = Math.min(Configuration.SERVER.storingAmount.get(), EnchantmentUtils.getPlayerXP(player));
					else if(Configuration.SERVER.storeUntilPreviousLevel.get())
					{
						int xpForCurrentLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);

						xpToStore = EnchantmentUtils.getPlayerXP(player) - xpForCurrentLevel;

						if(xpToStore == 0 && player.experienceLevel > 0) //player has exactly x > 0 levels (xp bar looks empty)
							xpToStore = xpForCurrentLevel - EnchantmentUtils.getExperienceForLevel(player.experienceLevel - 1);
					}
					else
						xpToStore = EnchantmentUtils.getPlayerXP(player);

					if(xpToStore == 0)
						return InteractionResult.PASS;

					te.addXP(xpToStore); //store as much XP as possible
					EnchantmentUtils.addPlayerXP(player, -xpToStore); //negative value removes xp
					return InteractionResult.SUCCESS;
				}
				else if(!player.isShiftKeyDown())
				{
					if(Configuration.SERVER.retrievalAmount.get() != -1)
					{
						int xpRetrieved = te.removeXP(Configuration.SERVER.retrievalAmount.get());

						EnchantmentUtils.addPlayerXP(player, xpRetrieved);
					}
					else if(Configuration.SERVER.retriveUntilNextLevel.get())
					{
						int xpToRetrieve = EnchantmentUtils.getExperienceForLevel(player.experienceLevel + 1) - EnchantmentUtils.getPlayerXP(player);
						int actuallyRemoved = te.removeXP(xpToRetrieve);

						EnchantmentUtils.addPlayerXP(player, actuallyRemoved);
					}
					else
					{
						EnchantmentUtils.addPlayerXP(player, (int)Math.ceil(te.getStoredXP()));
						te.setStoredXP(0);
					}

					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
	{
		BlockEntity tile = level.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity te)
			return Math.min(15, Math.floorDiv(te.getStoredXP(), Configuration.SERVER.xpForComparator.get()));
		else return 0;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(level.isClientSide || !stack.hasTag())
			return;

		BlockEntity tile = level.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity te)
		{
			CompoundTag tag = stack.getTag().getCompound("BlockEntityTag");

			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());
			te.load(tag);
			te.setChanged();
			level.sendBlockUpdated(pos, state, state, 2);
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		BlockEntity tile = level.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity te)
			te.setDestroyedByCreativePlayer(player.isCreative());

		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() == newState.getBlock())
			return;

		BlockEntity tile = level.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity te)
		{
			ItemStack stack = new ItemStack(asItem());

			if(te.getStoredLevels() != 0)
			{
				CompoundTag stackTag = new CompoundTag();

				stackTag.put("BlockEntityTag", te.save(new CompoundTag()));
				stack.setTag(stackTag);
				popResource(level, pos, stack);
			}
			else if(!te.isDestroyedByCreativePlayer())
				popResource(level, pos, stack);
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, block, fromPos, isMoving);

		level.setBlockAndUpdate(pos, state.setValue(POWERED, level.hasNeighborSignal(pos)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new XPBlockTileEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return level.isClientSide ? null : createTickerHelper(type, GlobalXP.teTypeXpBlock, XPBlockTileEntity::serverTick);
	}
}

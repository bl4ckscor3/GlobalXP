package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.compat.ITOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import openmods.utils.EnchantmentUtils;

public class XPBlock extends Block implements ITOPInfoProvider
{
	private static final VoxelShape SHAPE = Block.box(0.0001D, 0.0001D, 0.0001D, 15.999D, 15.999D, 15.999D);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public XPBlock()
	{
		super(Block.Properties.of(Material.METAL).strength(12.5F, 2000.0F).sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		BlockEntity tile = world.getBlockEntity(pos);

		if(tile instanceof XPBlockTileEntity)
		{
			if(!world.isClientSide)
			{
				XPBlockTileEntity te = (XPBlockTileEntity)tile;

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
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos)
	{
		BlockEntity te = world.getBlockEntity(pos);

		if(te instanceof XPBlockTileEntity)
			return Math.min(15, Math.floorDiv(((XPBlockTileEntity)te).getStoredXP(), Configuration.SERVER.xpForComparator.get()));
		else return 0;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(world.isClientSide || !stack.hasTag())
			return;

		BlockEntity te = world.getBlockEntity(pos);

		if(te instanceof XPBlockTileEntity)
		{
			CompoundTag tag = stack.getTag().getCompound("BlockEntityTag");

			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());
			((XPBlockTileEntity)te).load(state, tag);
			((XPBlockTileEntity)te).setChanged();
			world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player)
	{
		BlockEntity te = world.getBlockEntity(pos);

		if(world.getBlockEntity(pos) instanceof XPBlockTileEntity)
			((XPBlockTileEntity)te).setDestroyedByCreativePlayer(player.isCreative());

		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() == newState.getBlock())
			return;

		BlockEntity te = world.getBlockEntity(pos);

		if(te instanceof XPBlockTileEntity)
		{
			ItemStack stack = new ItemStack(asItem());

			if(((XPBlockTileEntity)te).getStoredLevels() != 0)
			{
				CompoundTag stackTag = new CompoundTag();

				stackTag.put("BlockEntityTag", ((XPBlockTileEntity)te).save(new CompoundTag()));
				stack.setTag(stackTag);
				popResource(world, pos, stack);
			}
			else if(!((XPBlockTileEntity)te).isDestroyedByCreativePlayer())
				popResource(world, pos, stack);
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);

		world.setBlockAndUpdate(pos, state.setValue(POWERED, world.hasNeighborSignal(pos)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world)
	{
		return new XPBlockTileEntity();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data)
	{
		BlockEntity te = world.getBlockEntity(data.getPos());

		if(te instanceof XPBlockTileEntity)
		{
			probeInfo.horizontal().text(new TranslatableComponent("info.globalxp.levels", String.format("%.2f", ((XPBlockTileEntity)te).getStoredLevels())));

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(new TranslatableComponent("info.globalxp.xp", ((XPBlockTileEntity)te).getStoredXP()));
		}
	}
}

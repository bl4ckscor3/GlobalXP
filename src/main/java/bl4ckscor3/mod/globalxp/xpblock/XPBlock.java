package bl4ckscor3.mod.globalxp.xpblock;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.compat.ITOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import openmods.utils.EnchantmentUtils;

public class XPBlock extends Block implements ITOPInfoProvider
{
	private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0001D, 0.0001D, 0.0001D, 15.999D, 15.999D, 15.999D);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public XPBlock()
	{
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(12.5F, 2000.0F).sound(SoundType.METAL));
		setDefaultState(stateContainer.getBaseState().with(POWERED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		TileEntity tile = world.getTileEntity(pos);

		if(tile instanceof XPBlockTileEntity)
		{
			if(!world.isRemote)
			{
				XPBlockTileEntity te = (XPBlockTileEntity)tile;

				if(player.isSneaking())
				{
					int xpToStore = 0;

					if(Configuration.SERVER.storeUntilPreviousLevel.get())
					{
						int xpForCurrentLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);

						xpToStore = EnchantmentUtils.getPlayerXP(player) - xpForCurrentLevel;

						if(xpToStore == 0 && player.experienceLevel > 0) //player has exactly x > 0 levels (xp bar looks empty)
							xpToStore = xpForCurrentLevel - EnchantmentUtils.getExperienceForLevel(player.experienceLevel - 1);
					}
					else
						xpToStore = EnchantmentUtils.getPlayerXP(player);

					if(xpToStore == 0)
						return ActionResultType.PASS;

					te.addXP(xpToStore); //store as much XP as possible
					EnchantmentUtils.addPlayerXP(player, -xpToStore); //negative value removes xp
					return ActionResultType.SUCCESS;
				}
				else if(!player.isSneaking())
				{
					if(Configuration.SERVER.retriveUntilNextLevel.get())
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

					return ActionResultType.SUCCESS;
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);

		if(te instanceof XPBlockTileEntity)
			return Math.min(15, Math.floorDiv(((XPBlockTileEntity)te).getStoredXP(), Configuration.SERVER.xpForComparator.get()));
		else return 0;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(world.isRemote || !stack.hasTag())
			return;

		TileEntity te = world.getTileEntity(pos);

		if(te instanceof XPBlockTileEntity)
		{
			CompoundNBT tag = stack.getTag().getCompound("BlockEntityTag");

			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());
			((XPBlockTileEntity)te).read(tag);
			((XPBlockTileEntity)te).markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		}
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		TileEntity te = world.getTileEntity(pos);

		if(world.getTileEntity(pos) instanceof XPBlockTileEntity)
			((XPBlockTileEntity)te).setDestroyedByCreativePlayer(player.isCreative());

		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() == newState.getBlock())
			return;

		TileEntity te = world.getTileEntity(pos);

		if(te instanceof XPBlockTileEntity)
		{
			ItemStack stack = new ItemStack(asItem());

			if(((XPBlockTileEntity)te).getStoredLevels() != 0)
			{
				CompoundNBT stackTag = new CompoundNBT();

				stackTag.put("BlockEntityTag", ((XPBlockTileEntity)te).write(new CompoundNBT()));
				stack.setTag(stackTag);
				spawnAsEntity(world, pos, stack);
			}
			else if(!((XPBlockTileEntity)te).isDestroyedByCreativePlayer())
				spawnAsEntity(world, pos, stack);
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);

		world.setBlockState(pos, state.with(POWERED, world.isBlockPowered(pos)));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new XPBlockTileEntity();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
	{
		TileEntity te = world.getTileEntity(data.getPos());

		if(te instanceof XPBlockTileEntity)
		{
			probeInfo.horizontal().text(new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", ((XPBlockTileEntity)te).getStoredLevels())).getFormattedText());

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(new TranslationTextComponent("info.globalxp.xp", ((XPBlockTileEntity)te).getStoredXP()).getFormattedText());
		}
	}
}

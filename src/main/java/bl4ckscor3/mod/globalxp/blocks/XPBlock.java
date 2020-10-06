package bl4ckscor3.mod.globalxp.blocks;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.imc.top.ITOPInfoProvider;
import bl4ckscor3.mod.globalxp.tileentity.XPBlockTileEntity;
import bl4ckscor3.mod.globalxp.util.XPUtils;
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

	public XPBlock()
	{
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(12.5F, 2000.0F).sound(SoundType.METAL));
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

				if(player.isCrouching()) //add all player xp to the block
				{
					int playerXP = EnchantmentUtils.getPlayerXP(player);

					((XPBlockTileEntity)world.getTileEntity(pos)).addXP(playerXP);
					EnchantmentUtils.addPlayerXP(player, -playerXP); // set player xp to 0
				}
				else //not sneaking = remove exactly enough xp from the block to get player to the next level
				{
					int neededXP = XPUtils.getXPToNextLevel(EnchantmentUtils.getPlayerXP(player));
					int availableXP = te.removeXP(neededXP);

					EnchantmentUtils.addPlayerXP(player, availableXP);
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
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
			return Math.min(15, Math.floorDiv(((XPBlockTileEntity)te).getStoredXP(), Configuration.CONFIG.xpForComparator.get()));
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

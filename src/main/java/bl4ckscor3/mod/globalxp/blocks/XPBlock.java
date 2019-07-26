package bl4ckscor3.mod.globalxp.blocks;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.imc.top.ITOPInfoProvider;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import openmods.utils.EnchantmentUtils;

public class XPBlock extends Block implements ITOPInfoProvider
{
	public XPBlock()
	{
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(12.5F, 2000.0F).sound(SoundType.METAL));

		setRegistryName(GlobalXP.MOD_ID + ":xp_block");
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if(world.isRemote || !stack.hasTag())
			return;

		TileEntity te = world.getTileEntity(pos);

		if(te instanceof TileEntityXPBlock)
		{
			CompoundNBT tag = stack.getTag().getCompound("BlockEntityTag");

			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());
			((TileEntityXPBlock)te).read(tag);
			((TileEntityXPBlock)te).markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		}
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!world.isRemote && hand == Hand.MAIN_HAND)
		{
			if(player.isSneaking()) //sneaking = add all player xp to the block
			{
				int playerXP = EnchantmentUtils.getPlayerXP(player);

				((TileEntityXPBlock)world.getTileEntity(pos)).addXP(playerXP);
				EnchantmentUtils.addPlayerXP(player, -playerXP); // set player xp to 0
			}
			else //not sneaking = remove exactly enough xp from the block to get player to the next level
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)world.getTileEntity(pos));
				int neededXP = XPUtils.getXPToNextLevel(EnchantmentUtils.getPlayerXP(player));
				int availableXP = te.removeXP(neededXP);

				EnchantmentUtils.addPlayerXP(player, availableXP);
			}

			return true;
		}

		return false;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		TileEntity te = world.getTileEntity(pos);

		if(world.getTileEntity(pos) instanceof TileEntityXPBlock)
			((TileEntityXPBlock)te).setDestroyedByCreativePlayer(player.isCreative());
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		TileEntity te = world.getTileEntity(pos);

		if(te instanceof TileEntityXPBlock)
		{
			ItemStack stack = new ItemStack(asItem());

			if(((TileEntityXPBlock)te).getStoredLevels() != 0)
			{
				CompoundNBT stackTag = new CompoundNBT();

				stackTag.put("BlockEntityTag", ((TileEntityXPBlock)te).write(new CompoundNBT()));
				stack.setTag(stackTag);
				spawnAsEntity(world, pos, stack);
			}
			else if(!((TileEntityXPBlock)te).isDestroyedByCreativePlayer())
				spawnAsEntity(world, pos, stack);
		}

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileEntityXPBlock();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
	{
		TileEntity te = world.getTileEntity(data.getPos());

		if(te instanceof TileEntityXPBlock)
		{
			probeInfo.horizontal().text(new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", ((TileEntityXPBlock)te).getStoredLevels())).getFormattedText());

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(new TranslationTextComponent("info.globalxp.xp", ((TileEntityXPBlock)te).getStoredXP()).getFormattedText());
		}
	}
}

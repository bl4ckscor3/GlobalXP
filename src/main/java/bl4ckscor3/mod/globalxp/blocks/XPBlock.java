package bl4ckscor3.mod.globalxp.blocks;

import java.util.Random;

import bl4ckscor3.mod.globalxp.imc.top.ITOPInfoProvider;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class XPBlock extends Block implements ITOPInfoProvider
{
	public XPBlock(Material materialIn)
	{
		super(materialIn);

		setCreativeTab(CreativeTabs.MISC);
		setHardness(12.5F);
		setResistance(2000.0F);
		setSoundType(SoundType.METAL);
		setTranslationKey("xp_block");
		setRegistryName("xp_block");
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(world.isRemote || !stack.hasTagCompound())
			return;

		TileEntity te = world.getTileEntity(pos);

		if(te instanceof TileEntityXPBlock)
		{
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("BlockEntityTag");

			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			((TileEntityXPBlock)te).readFromNBT(tag);
			((TileEntityXPBlock)te).markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
		}
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		TileEntity te = world.getTileEntity(pos);

		if(world.getTileEntity(pos) instanceof TileEntityXPBlock)
			((TileEntityXPBlock)te).setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) //shamelessly stolen from shulker box
	{
		TileEntity te = world.getTileEntity(pos);

		if(te instanceof TileEntityXPBlock)
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

			if(((TileEntityXPBlock)te).getStoredLevels() != 0)
			{
				NBTTagCompound stackTag = new NBTTagCompound();

				stackTag.setTag("BlockEntityTag", ((TileEntityXPBlock)te).writeToNBT(new NBTTagCompound()));
				stack.setTagCompound(stackTag);
				spawnAsEntity(world, pos, stack);
			}
			else if(!((TileEntityXPBlock)te).isDestroyedByCreativePlayer())
				spawnAsEntity(world, pos, stack);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Gets the stack that is being displayed in the WAILA toolip
	 */
	public ItemStack getWailaDisplayStack()
	{
		return new ItemStack(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityXPBlock();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
	{
		TileEntity te = world.getTileEntity(data.getPos());

		if(te instanceof TileEntityXPBlock)
		{
			probeInfo.horizontal().text(I18n.format("info.levels", String.format("%.2f", ((TileEntityXPBlock)te).getStoredLevels())));

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(I18n.format("info.xp", ((TileEntityXPBlock)te).getStoredXP()));
		}
	}
}

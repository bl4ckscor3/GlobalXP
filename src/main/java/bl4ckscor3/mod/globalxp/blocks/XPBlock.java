package bl4ckscor3.mod.globalxp.blocks;

import java.util.Random;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.imc.top.ITOPInfoProvider;
import bl4ckscor3.mod.globalxp.network.packets.SPacketUpdateXPBlock;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class XPBlock extends Block implements ITOPInfoProvider
{
	public XPBlock(Material materialIn)
	{
		super(materialIn);

		setCreativeTab(CreativeTabs.MISC);
		setHardness(12.5F);
		setResistance(2000.0F);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("xp_block");
		setRegistryName("xp_block");
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(worldIn.isRemote || !stack.hasTagCompound())
			return;

		TileEntity te = worldIn.getTileEntity(pos);

		if(te instanceof TileEntityXPBlock)
		{
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("BlockEntityTag");

			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			((TileEntityXPBlock)te).readFromNBT(tag);
			((TileEntityXPBlock)te).markDirty();
			GlobalXP.network.sendToAllAround(new SPacketUpdateXPBlock((TileEntityXPBlock)te), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) //shamelessly stolen from shulker box
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityXPBlock)
		{
			ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));

			if(((TileEntityXPBlock)tileentity).getStoredLevels() != 0)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();

				nbttagcompound.setTag("BlockEntityTag", ((TileEntityXPBlock)tileentity).writeToNBT(nbttagcompound1));
				itemstack.setTagCompound(nbttagcompound);
			}

			spawnAsEntity(worldIn, pos, itemstack);
		}

		super.breakBlock(worldIn, pos, state);
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
	public BlockRenderLayer getBlockLayer()
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
			TileEntityXPBlock tileentity = (TileEntityXPBlock)te;

			probeInfo.horizontal().text(I18n.format("info.body", String.format("%.2f", tileentity.getStoredLevels())));

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(I18n.format("info.extended", tileentity.getStoredXP()));
		}
	}
}

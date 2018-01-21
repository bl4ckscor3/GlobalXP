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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		setTickRandomly(true);
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
		TileEntity te1 = world.getTileEntity(data.getPos());

		if(te1 instanceof TileEntityXPBlock)
		{
			TileEntityXPBlock te2 = (TileEntityXPBlock)te1;

			probeInfo.horizontal().text(I18n.format("info.levels", String.format("%.2f", te2.getStoredLevels())));

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(I18n.format("info.xp", te2.getStoredXP()));
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TileEntityXPBlock)
		{
			switch((int)((TileEntityXPBlock) te).getStoredLevels())
			{
				case 0: this.lightValue = 0;
					break;
				case 1:
				case 2:
				case 3: this.lightValue = 5;
					break;
				case 4:
				case 5:	
				case 6: this.lightValue = 6;
					break;
				case 7:
				case 8:
				case 9: this.lightValue = 7;
					break;
				case 10:
				case 11:
				case 12: this.lightValue = 8;
					break;
				case 13:
				case 14:
				case 15: this.lightValue = 9;
					break;
				case 16:
				case 17:
				case 18: this.lightValue = 10;
					break;
				case 19:
				case 20:
				case 21: this.lightValue = 11;
					break;
				case 22:
				case 23:
				case 24: this.lightValue = 12;
					break;
				case 25:
				case 26:
				case 27: this.lightValue = 13;
					break;
				case 28:
				case 29: this.lightValue = 14;
					break;
				default: this.lightValue = 15;
					break;
			}
			return this.lightValue;
		}
		return super.getLightValue(state, world, pos);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		worldIn.scheduleUpdate(pos, this, 1);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		worldIn.checkLight(pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
		worldIn.checkLight(pos);
	}
}

package bl4ckscor3.mod.globalxp.blocks;

import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class XPBlock extends Block
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			if(playerIn.isSneaking()) //add all levels to the block
			{
				((TileEntityXPBlock)worldIn.getTileEntity(pos)).addLevel(playerIn.experienceLevel);
				playerIn.removeExperienceLevel(playerIn.experienceLevel);
			}
			else //remove one level from the block
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)worldIn.getTileEntity(pos));
				
				if(te.getStoredLevels() == 0)
					return true;
				
				te.removeLevel();
				playerIn.addExperienceLevel(1);
			}
		}
		
		return true;
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
}

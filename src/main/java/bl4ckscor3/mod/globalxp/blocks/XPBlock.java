package bl4ckscor3.mod.globalxp.blocks;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import bl4ckscor3.mod.globalxp.network.packets.PacketHelper;
import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class XPBlock extends Block implements BlockEntityProvider {
	
	public static final Identifier ID = new Identifier(GlobalXP.MODID + ":xp_block");

	public XPBlock() {
		super(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(12.5F, 2000.0F).build());
	}
	
	@Override
	public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, Direction direction, float f1, float f2, float f3) {
		if(!(blockState.getBlock() instanceof XPBlock) || hand != Hand.MAIN)
			return false;
		
		if(!world.isClient) 
		{
			XPBlockEntity be = ((XPBlockEntity) world.getBlockEntity(blockPos));

			if(player.isSneaking()) //sneaking = add all player xp to the block
			{
				be.addXP(player.experienceLevel);
				player.addExperience(-player.experienceLevel - 1); // set player xp to 0
				return true;
			}
			else //not sneaking = remove exactly enough xp from the block to get player to the next level
			{
				int neededXP = player.method_7349() - (int)player.experience;
				int availableXP = be.removeXP(neededXP);

				player.addExperience(availableXP);
				return true;
			}
		}
		
		return false;
	}

	public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		if(world.isClient || world.getBlockEntity(blockPos) == null || !(livingEntity instanceof PlayerEntity))
			return;

		BlockEntity be = world.getBlockEntity(blockPos);
		ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;

		if(be instanceof XPBlockEntity)
		{
			CompoundTag tag = world.getBlockEntity(blockPos).toTag(new CompoundTag());

			tag.putInt("x", blockPos.getX());
			tag.putInt("y", blockPos.getY());
			tag.putInt("z", blockPos.getZ());
			be.fromTag(tag);
			be.markDirty();
			player.networkHandler.sendPacket(PacketHelper.createUpdateXPPacket(blockPos, ((XPBlockEntity) be).getStoredXP()));
		}
	}
	
	@Override
	public void onBreak(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) //shamelessly stolen from shulker box
	{
		BlockEntity blockentity = worldIn.getBlockEntity(pos);

		if (blockentity instanceof XPBlockEntity)
		{
			ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));

			if(((XPBlockEntity)blockentity).getStoredLevels() != 0)
			{
				CompoundTag nbttagcompound = new CompoundTag();
				CompoundTag nbttagcompound1 = new CompoundTag();

				nbttagcompound.put("BlockEntityTag", ((XPBlockEntity)blockentity).toTag(nbttagcompound1));
				itemstack.setTag(nbttagcompound);
			}

			dropStack(worldIn, pos, itemstack);
		}

		super.onBreak(worldIn, pos, state, player);
	}
	
	public BlockRenderLayer getRenderLayer() 
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasBlockEntity()
	{
		return true;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new XPBlockEntity();
	}

}

package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GlobalXP implements ModInitializer {
	
	public static final String MODID = "globalxp";
	public static final String NAME = "Global XP";
	public static final String VERSION = "1.0.0";
	
	public static final Identifier UPDATE_XP_BLOCK = new Identifier(MODID, "update_xp_block");
	
	public static final XPBlock XP_BLOCK = new XPBlock();

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "item_group"), () -> new ItemStack(XP_BLOCK));
	public static final BlockEntityType<XPBlockEntity> XP_BLOCK_ENTITY = BlockEntityType.Builder.create(XPBlockEntity::new).method_11034(null);

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, XPBlock.ID, XP_BLOCK);
		Registry.register(Registry.BLOCK_ENTITY, MODID + ":xp_block", XP_BLOCK_ENTITY);
		Registry.register(Registry.ITEM, MODID + ":xp_block", new BlockItem(XP_BLOCK, new Item.Settings().itemGroup(ITEM_GROUP)));

		
		CustomPayloadPacketRegistry.CLIENT.register(UPDATE_XP_BLOCK, (packetContext, packetByteBuf) -> {
			PlayerEntity player = packetContext.getPlayer();
			World world = player.getEntityWorld();
			BlockPos pos = BlockPos.fromLong(packetByteBuf.readLong());

			if(world.getBlockState(pos).getBlock() instanceof XPBlock) {
				((XPBlockEntity) player.getEntityWorld().getBlockEntity(pos)).addXP(packetByteBuf.readInt());
			}
		});
	}

}

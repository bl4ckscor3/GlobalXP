package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.renderers.XPBlockRenderer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GlobalXP implements ModInitializer {
	
	public static final String MODID = "globalxp";
	public static final String NAME = "Global XP";
	public static final String VERSION = "1.4";
	
	public static final Identifier UPDATE_XP_BLOCK = new Identifier(MODID, "update_xp_block");
	
	public static final XPBlock XP_BLOCK = new XPBlock();

	public static final BlockEntityType<XPBlockEntity> XP_BLOCK_ENTITY = BlockEntityType.Builder.create(XPBlockEntity::new).method_11034(null);

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, XPBlock.ID, XP_BLOCK);
		Registry.register(Registry.BLOCK_ENTITY, XPBlock.ID, XP_BLOCK_ENTITY);
		Registry.register(Registry.ITEM, XPBlock.ID, new BlockItem(XP_BLOCK, new Item.Settings().itemGroup(ItemGroup.MISC)));
		BlockEntityRendererRegistry.INSTANCE.register(XPBlockEntity.class, new XPBlockRenderer());
		
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

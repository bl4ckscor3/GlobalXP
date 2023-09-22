package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GlobalXP implements ModInitializer {
	public static final String MOD_ID = "globalxp";
	public static final XPBlock XP_BLOCK = new XPBlock(BlockBehaviour.Properties.of().strength(12.5F, 2000.0F).sound(SoundType.METAL));
	public static final BlockEntityType<XPBlockEntity> XP_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.of(XPBlockEntity::new, XP_BLOCK).build(null);
	public static final XPBlockItem XP_BLOCK_ITEM = new XPBlockItem(XP_BLOCK);
	public static final Configuration CONFIG = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();

	@Override
	public void onInitialize() {
		Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "xp_block"), XP_BLOCK);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(MOD_ID, "xp_block"), XP_BLOCK_ENTITY_TYPE);
		Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MOD_ID, "xp_block"), XP_BLOCK_ITEM);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.accept(XP_BLOCK_ITEM));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(XP_BLOCK_ITEM));
	}
}

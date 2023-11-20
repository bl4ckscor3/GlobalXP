package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.compat.GetTheOneProbe;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(GlobalXP.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class GlobalXP {
	public static final String MOD_ID = "globalxp";
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
	public static final DeferredBlock<XPBlock> XP_BLOCK = BLOCKS.register("xp_block", () -> new XPBlock(BlockBehaviour.Properties.of().strength(12.5F, 2000.0F).sound(SoundType.METAL)));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<XPBlockEntity>> XP_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("xp_block", () -> BlockEntityType.Builder.of(XPBlockEntity::new, XP_BLOCK.get()).build(null));
	public static final DeferredItem<XPBlockItem> XP_BLOCK_ITEM = ITEMS.register("xp_block", () -> new XPBlockItem(XP_BLOCK.get()));

	public GlobalXP(IEventBus modEventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.SERVER_SPEC);
		BLOCKS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		ITEMS.register(modEventBus);
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded("theoneprobe"))
			InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS || event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
			event.accept(XP_BLOCK_ITEM.get());
	}
}

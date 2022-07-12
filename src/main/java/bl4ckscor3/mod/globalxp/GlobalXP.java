package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.compat.GetTheOneProbe;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(GlobalXP.MOD_ID)
@EventBusSubscriber(bus=Bus.MOD)
public class GlobalXP
{
	public static final String MOD_ID = "globalxp";
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
	public static final RegistryObject<XPBlock> XP_BLOCK = BLOCKS.register("xp_block", () -> new XPBlock(Block.Properties.of(Material.METAL).strength(12.5F, 2000.0F).sound(SoundType.METAL)));
	public static final RegistryObject<BlockEntityType<XPBlockEntity>> XP_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("xp_block", () -> BlockEntityType.Builder.of(XPBlockEntity::new, XP_BLOCK.get()).build(null));
	public static final RegistryObject<XPBlockItem> XP_BLOCK_ITEMS = ITEMS.register("xp_block", () -> new XPBlockItem(XP_BLOCK.get()));

	public GlobalXP()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.SERVER_SPEC);
		BLOCKS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		ITEMS.register(modEventBus);
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event)
	{
		if(ModList.get().isLoaded("theoneprobe"))
			InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}
}

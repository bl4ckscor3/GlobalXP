package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.imc.top.GetTheOneProbe;
import bl4ckscor3.mod.globalxp.itemblocks.XPItemBlock;
import bl4ckscor3.mod.globalxp.tileentity.XPBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ObjectHolder;

@Mod(GlobalXP.MOD_ID)
@EventBusSubscriber(bus=Bus.MOD)
public class GlobalXP
{
	public static final String MOD_ID = "globalxp";
	@ObjectHolder(MOD_ID + ":xp_block")
	public static Block xp_block;
	@ObjectHolder(MOD_ID + ":xp_block")
	public static TileEntityType<XPBlockTileEntity> teTypeXpBlock;

	public GlobalXP()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event)
	{
		if(ModList.get().isLoaded("theoneprobe"))
			InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(new XPBlock().setRegistryName("xp_block"));
	}

	@SubscribeEvent
	public static void onRegisterTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register(TileEntityType.Builder.create(XPBlockTileEntity::new, xp_block).build(null).setRegistryName(new ResourceLocation(xp_block.getRegistryName().toString())));
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new XPItemBlock(xp_block).setRegistryName(xp_block.getRegistryName()));
	}
}

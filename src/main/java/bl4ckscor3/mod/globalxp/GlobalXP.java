package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.imc.top.GetTheOneProbe;
import bl4ckscor3.mod.globalxp.itemblocks.ItemBlockXPBlock;
import bl4ckscor3.mod.globalxp.network.packets.RequestXPBlockUpdate;
import bl4ckscor3.mod.globalxp.network.packets.UpdateXPBlock;
import bl4ckscor3.mod.globalxp.renderer.TileEntityXPBlockRenderer;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(GlobalXP.MOD_ID)
@EventBusSubscriber(bus=Bus.MOD)
public class GlobalXP
{
	public static final String MOD_ID = "globalxp";
	public static final String PROTOCOL_VERSION = "1.0"; //for channel
	public static Block xp_block;
	public static TileEntityType<TileEntityXPBlock> teTypeXpBlock;
	public static SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public GlobalXP()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CONFIG_SPEC);
		MinecraftForge.EVENT_BUS.addListener(this::onModelRegistry);
		MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
	}

	@SubscribeEvent
	public static void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		int index = 0;

		channel.registerMessage(index++, RequestXPBlockUpdate.class, RequestXPBlockUpdate::encode, RequestXPBlockUpdate::decode, RequestXPBlockUpdate::onMessage);
		channel.registerMessage(index++, UpdateXPBlock.class, UpdateXPBlock::encode, UpdateXPBlock::decode, UpdateXPBlock::onMessage);
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event)
	{
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(xp_block = new XPBlock());
	}

	@SubscribeEvent
	public static void onRegisterTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
	{
		teTypeXpBlock = TileEntityType.register(xp_block.getRegistryName().toString(), TileEntityType.Builder.create(TileEntityXPBlock::new));
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemBlockXPBlock(xp_block).setRegistryName(xp_block.getRegistryName()));
	}

	public void onModelRegistry(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXPBlock.class, new TileEntityXPBlockRenderer());
	}

	public void onRightClickBlock(RightClickBlock event)
	{
		if(!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof XPBlock) || event.getHand() != EnumHand.MAIN_HAND)
			return;

		if(!event.getWorld().isRemote)
		{
			if(event.getEntityPlayer().isSneaking()) //sneaking = add all player xp to the block
			{
				((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos())).addXP(event.getEntityPlayer().experienceTotal);
				event.getEntityPlayer().addExperienceLevel(-event.getEntityPlayer().experienceLevel - 1); // set player xp to 0
			}
			else //not sneaking = remove exactly enough xp from the block to get player to the next level
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos()));
				EntityPlayer player = event.getEntityPlayer();
				int neededXP = player.xpBarCap() - (int)player.experience;
				int availableXP = te.removeXP(neededXP);

				player.giveExperiencePoints(availableXP);
			}
		}
	}
}

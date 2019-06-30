package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.renderer.TileEntityXPBlockRenderer;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=GlobalXP.MOD_ID, value=Side.CLIENT)
public class ClientReg
{
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(GlobalXP.xp_block), 0, new ModelResourceLocation("globalxp:xp_block", "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXPBlock.class, new TileEntityXPBlockRenderer());
	}
}

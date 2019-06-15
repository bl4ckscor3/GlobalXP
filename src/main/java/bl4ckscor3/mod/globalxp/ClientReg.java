package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.renderer.TileEntityXPBlockRenderer;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value=Dist.CLIENT)
public class ClientReg
{
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXPBlock.class, new TileEntityXPBlockRenderer());
	}
}

package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.renderer.XPBlockTileEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=GlobalXP.MOD_ID, value=Dist.CLIENT, bus=Bus.MOD)
public class ClientReg
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(GlobalXP.teTypeXpBlock, XPBlockTileEntityRenderer::new);
		RenderTypeLookup.setRenderLayer(GlobalXP.xp_block, RenderType.cutout());
	}
}

package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.client.XPBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=GlobalXP.MOD_ID, value=Dist.CLIENT, bus=Bus.MOD)
public class ClientReg
{
	@SubscribeEvent
	public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(GlobalXP.XP_BLOCK_ENTITY_TYPE.get(), XPBlockEntityRenderer::new);
	}
}

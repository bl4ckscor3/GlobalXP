package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.client.XPBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = GlobalXP.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class NeoClientEntrypoint {
	public NeoClientEntrypoint(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(GlobalXP.XP_BLOCK_ENTITY_TYPE.get(), XPBlockEntityRenderer::new);
	}
}

package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.client.XPBlockEntityRenderer;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class FabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigScreenFactoryRegistry.INSTANCE.register(GlobalXP.MODID, ConfigurationScreen::new);
		BlockEntityRenderers.register(GlobalXP.XP_BLOCK_ENTITY_TYPE.get(), XPBlockEntityRenderer::new);
	}
}

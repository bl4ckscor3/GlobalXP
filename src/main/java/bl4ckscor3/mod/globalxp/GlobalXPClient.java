package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.client.XPBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GlobalXPClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(GlobalXP.XP_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockEntityRenderers.register(GlobalXP.XP_BLOCK_ENTITY_TYPE, XPBlockEntityRenderer::new);
	}
}

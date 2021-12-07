package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.client.XPBlockEntityRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=GlobalXP.MOD_ID, value=Dist.CLIENT, bus=Bus.MOD)
public class ClientReg
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->	BlockEntityRenderers.register(GlobalXP.beTypeXpBlock, XPBlockEntityRenderer::new));
		ItemBlockRenderTypes.setRenderLayer(GlobalXP.xp_block, RenderType.cutout());
	}
}

package bl4ckscor3.mod.globalxp.renderer;

import org.lwjgl.opengl.GL11;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ForgeHooksClient;

public class TileEntityXPBlockRenderer extends TileEntityRenderer<TileEntityXPBlock>
{
	private ItemStack emerald = new ItemStack(Items.EMERALD, 1);

	@Override
	public void render(TileEntityXPBlock te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(Configuration.CONFIG.renderNameplate.get())
		{
			ITextComponent levelsText = new TextComponentString((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")");

			if(te != null && te.getPos() != null && rendererDispatcher.cameraHitResult != null && rendererDispatcher.cameraHitResult.getBlockPos() != null && rendererDispatcher.cameraHitResult.getBlockPos().equals(te.getPos()))
			{
				setLightmapDisabled(true);
				drawNameplate(te, levelsText.getFormattedText(), x, y, z, 12);
				setLightmapDisabled(false);
			}
		}

		double offset = Math.sin((te.getWorld().getWorldInfo().getGameTime() + partialTicks) * Configuration.CONFIG.bobSpeed.get() / 8.0D) / 10.0D;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(emerald, te.getWorld(), null);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.pushMatrix();
		GlStateManager.translated(x + 0.5D, y + 0.4D + offset, z + 0.5D);
		GlStateManager.rotatef((te.getWorld().getWorldInfo().getGameTime() + partialTicks) * 4.0F * Configuration.CONFIG.spinSpeed.get().floatValue(), 0.0F, 1.0F, 0.0F);
		model = ForgeHooksClient.handleCameraTransforms(model, TransformType.GROUND, false);
		Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().getItemRenderer().renderItem(emerald, model);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
}

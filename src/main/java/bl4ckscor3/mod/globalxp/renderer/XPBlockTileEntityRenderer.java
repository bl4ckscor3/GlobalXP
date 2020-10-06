package bl4ckscor3.mod.globalxp.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.tileentity.XPBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.StringTextComponent;

public class XPBlockTileEntityRenderer extends TileEntityRenderer<XPBlockTileEntity>
{
	private ItemStack emerald = new ItemStack(Items.EMERALD, 1);

	public XPBlockTileEntityRenderer(TileEntityRendererDispatcher terd)
	{
		super(terd);
	}

	@Override
	public void render(XPBlockTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		if(Configuration.CONFIG.renderNameplate.get())
		{
			RayTraceResult rtr = renderDispatcher.cameraHitResult;

			if(te != null && te.getPos() != null && rtr != null && rtr.getType() == Type.BLOCK && ((BlockRayTraceResult)rtr).getPos() != null && ((BlockRayTraceResult)rtr).getPos().equals(te.getPos()))
			{
				StringTextComponent levelsString = new StringTextComponent((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")");
				float opacity = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
				int j = (int)(opacity * 255.0F) << 24;
				FontRenderer fontRenderer = renderDispatcher.fontRenderer;
				float halfWidth = -fontRenderer.getStringPropertyWidth(levelsString) / 2;
				Matrix4f positionMatrix;

				stack.push();
				stack.translate(0.5D, 1.5D, 0.5D);
				stack.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
				stack.scale(-0.025F, -0.025F, 0.025F);
				positionMatrix = stack.getLast().getMatrix();
				fontRenderer.func_243247_a(levelsString, halfWidth, 0, 553648127, false, positionMatrix, buffer, true, j, combinedLight); //renderString
				fontRenderer.func_243247_a(levelsString, halfWidth, 0, -1, false, positionMatrix, buffer, false, 0, combinedLight);
				stack.pop();
			}
		}

		float time = te.getWorld().getWorldInfo().getGameTime() + partialTicks;
		double offset = Math.sin(time * Configuration.CONFIG.bobSpeed.get() / 8.0D) / 10.0D;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(emerald, te.getWorld(), null);

		stack.translate(0.5D, 0.4D + offset, 0.5D);
		stack.rotate(Vector3f.YP.rotationDegrees(time * 4.0F * Configuration.CONFIG.spinSpeed.get().floatValue()));
		Minecraft.getInstance().getItemRenderer().renderItem(emerald, TransformType.GROUND, false, stack, buffer, combinedLight, combinedOverlay, model);
	}
}

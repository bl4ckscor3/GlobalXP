package bl4ckscor3.mod.globalxp.xpblock.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockTileEntity;
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
		stack.pushPose();

		if(Configuration.CLIENT.renderNameplate.get())
		{
			RayTraceResult rtr = renderer.cameraHitResult;

			if(te != null && te.getBlockPos() != null && rtr != null && rtr.getType() == Type.BLOCK && ((BlockRayTraceResult)rtr).getBlockPos() != null && ((BlockRayTraceResult)rtr).getBlockPos().equals(te.getBlockPos()))
			{
				StringTextComponent levelsString = new StringTextComponent((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")");
				float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
				int j = (int)(opacity * 255.0F) << 24;
				FontRenderer fontRenderer = renderer.font;
				float halfWidth = -fontRenderer.width(levelsString) / 2;
				Matrix4f positionMatrix;

				stack.pushPose();
				stack.translate(0.5D, 1.5D, 0.5D);
				stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
				stack.scale(-0.025F, -0.025F, 0.025F);
				positionMatrix = stack.last().pose();
				fontRenderer.drawInBatch(levelsString, halfWidth, 0, 553648127, false, positionMatrix, buffer, true, j, combinedLight); //renderString
				fontRenderer.drawInBatch(levelsString, halfWidth, 0, -1, false, positionMatrix, buffer, false, 0, combinedLight);
				stack.popPose();
			}
		}

		float time = te.getLevel().getLevelData().getGameTime() + partialTicks;
		double offset = Math.sin(time * Configuration.CLIENT.bobSpeed.get() / 8.0D) / 10.0D;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(emerald, te.getLevel(), null);

		stack.translate(0.5D, 0.4D + offset, 0.5D);
		stack.mulPose(Vector3f.YP.rotationDegrees(time * 4.0F * Configuration.CLIENT.spinSpeed.get().floatValue()));
		Minecraft.getInstance().getItemRenderer().render(emerald, TransformType.GROUND, false, stack, buffer, combinedLight, combinedOverlay, model);
		stack.popPose();
	}
}

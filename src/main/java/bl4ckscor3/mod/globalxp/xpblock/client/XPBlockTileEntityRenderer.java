package bl4ckscor3.mod.globalxp.xpblock.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;

public class XPBlockTileEntityRenderer implements BlockEntityRenderer<XPBlockTileEntity>
{
	private ItemStack emerald = new ItemStack(Items.EMERALD, 1);

	public XPBlockTileEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(XPBlockTileEntity te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
	{
		stack.pushPose();

		if(Configuration.CLIENT.renderNameplate.get())
		{
			Minecraft mc = Minecraft.getInstance();
			HitResult rtr = mc.hitResult;

			if(te != null && te.getBlockPos() != null && rtr != null && rtr.getType() == Type.BLOCK && ((BlockHitResult)rtr).getBlockPos() != null && ((BlockHitResult)rtr).getBlockPos().equals(te.getBlockPos()))
			{
				TextComponent levelsString = new TextComponent((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")");
				float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
				int j = (int)(opacity * 255.0F) << 24;
				float halfWidth = -mc.font.width(levelsString) / 2;
				Matrix4f positionMatrix;

				stack.pushPose();
				stack.translate(0.5D, 1.5D, 0.5D);
				stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
				stack.scale(-0.025F, -0.025F, 0.025F);
				positionMatrix = stack.last().pose();
				mc.font.drawInBatch(levelsString, halfWidth, 0, 553648127, false, positionMatrix, buffer, true, j, combinedLight); //renderString
				mc.font.drawInBatch(levelsString, halfWidth, 0, -1, false, positionMatrix, buffer, false, 0, combinedLight);
				stack.popPose();
			}
		}

		float time = te.getLevel().getLevelData().getGameTime() + partialTicks;
		double offset = Math.sin(time * Configuration.CLIENT.bobSpeed.get() / 8.0D) / 10.0D;
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(emerald, te.getLevel(), null, 0);

		stack.translate(0.5D, 0.4D + offset, 0.5D);
		stack.mulPose(Vector3f.YP.rotationDegrees(time * 4.0F * Configuration.CLIENT.spinSpeed.get().floatValue()));
		Minecraft.getInstance().getItemRenderer().render(emerald, TransformType.GROUND, false, stack, buffer, combinedLight, combinedOverlay, model);
		stack.popPose();
	}
}

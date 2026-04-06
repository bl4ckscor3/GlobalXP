package bl4ckscor3.mod.globalxp.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class XPBlockEntityRenderer implements BlockEntityRenderer<XPBlockEntity, XPBlockRenderState> {
	private final ItemModelResolver itemModelResolver;

	public XPBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModelResolver = ctx.itemModelResolver();
	}

	@Override
	public void submit(XPBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		Minecraft mc = Minecraft.getInstance();

		poseStack.pushPose();

		if (state.lookingAtBlock) {
			float y = 1.5F;

			if (state.renderXPInfo) {
				renderTextAboveBlock(mc, poseStack, submitNodeCollector, state, camera, state.xpInfo, y);
				y += 0.25F;
			}

			if (state.renderCustomName)
				renderTextAboveBlock(mc, poseStack, submitNodeCollector, state, camera, state.customName, y);
		}

		poseStack.translate(0.5D, 0.4D + state.yOffset, 0.5D);
		poseStack.mulPose(state.rotation);
		state.emerald.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
	}

	@Override
	public XPBlockRenderState createRenderState() {
		return new XPBlockRenderState();
	}

	@Override
	public void extractRenderState(XPBlockEntity be, XPBlockRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		float time = be.getLevel().getLevelData().getGameTime() + partialTick;

		state.lookingAtBlock = Minecraft.getInstance().hitResult instanceof BlockHitResult hitResult && be.getBlockPos().equals(hitResult.getBlockPos());
		state.renderXPInfo = Configuration.CLIENT.renderXPInfo.get();
		state.xpInfo = state.renderXPInfo ? Component.literal((int) be.getStoredLevels() + " (" + be.getStoredXP() + ")").getVisualOrderText() : FormattedCharSequence.EMPTY;
		state.renderCustomName = be.hasCustomName() && Configuration.CLIENT.renderCustomName.get();
		state.customName = state.renderCustomName ? be.getCustomName().getVisualOrderText() : FormattedCharSequence.EMPTY;
		state.yOffset = Math.sin(time * Configuration.CLIENT.bobSpeed.get() / 8.0D) / 10.0D;
		state.rotation = Axis.YP.rotationDegrees(time * 4.0F * Configuration.CLIENT.spinSpeed.get().floatValue());
		itemModelResolver.updateForTopItem(state.emerald, new ItemStack(Items.EMERALD), ItemDisplayContext.GROUND, be.getLevel(), null, (int) be.getBlockPos().asLong());
		BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPosition, breakProgress);
	}

	public void renderTextAboveBlock(Minecraft mc, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, XPBlockRenderState state, CameraRenderState camera, FormattedCharSequence text, double y) {
		float opacity = mc.options.getBackgroundOpacity(0.25F);
		int backgroundColor = (int) (opacity * 255.0F) << 24;
		float halfWidth = -mc.font.width(text) / 2;

		poseStack.pushPose();
		poseStack.translate(0.5D, y, 0.5D);
		poseStack.mulPose(camera.orientation);
		poseStack.scale(0.025F, -0.025F, 0.025F);
		submitNodeCollector.submitText(poseStack, halfWidth, 0.0F, text, false, DisplayMode.SEE_THROUGH, state.lightCoords, 553648127, backgroundColor, 0);
		submitNodeCollector.submitText(poseStack, halfWidth, 0.0F, text, false, DisplayMode.NORMAL, state.lightCoords, -1, backgroundColor, 0);
		poseStack.popPose();
	}
}

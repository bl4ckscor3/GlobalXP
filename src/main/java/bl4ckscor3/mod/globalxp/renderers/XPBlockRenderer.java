package bl4ckscor3.mod.globalxp.renderers;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class XPBlockRenderer extends BlockEntityRenderer<XPBlockEntity>
{
	@Override
	public void render(XPBlockEntity blockEntity, double x, double y, double z, float partialTicks, int int_1)
	{
		double offset = Math.sin((blockEntity.getWorld().getTime() + partialTicks) * 1.0D / 8.0D) / 10.0D;
		BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModelMap().getModel(Items.EMERALD);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.pushMatrix();
		GlStateManager.translated(x + 0.5D, y + 0.5D + offset, z + 0.5D);
		GlStateManager.scalef(0.75F, 0.75F, 0.75F);
		GlStateManager.rotatef((blockEntity.getWorld().getTime() + partialTicks) * 4.0F * 1.0F, 0.0F, 1.0F, 0.0F);
		MinecraftClient.getInstance().getItemRenderer().renderItemAndGlow(new ItemStack(Items.EMERALD), model);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

}

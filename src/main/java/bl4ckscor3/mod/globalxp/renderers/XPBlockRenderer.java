package bl4ckscor3.mod.globalxp.renderers;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;

@Environment(EnvType.CLIENT)
public class XPBlockRenderer extends BlockEntityRenderer<XPBlockEntity>
{
	private final ItemStack emerald = new ItemStack(Items.EMERALD, 1);
	private final float spinSpeed = 1.0F;
	private final double bobSpeed = 1.0D;

	@Override
	public void render(XPBlockEntity be, double x, double y, double z, float partialTicks, int int_1)
	{
		//TODO: config option to disable this
		TextComponent levelsText = new StringTextComponent((int)be.getStoredLevels() + " (" + be.getStoredXP() + ")");

		if(be != null && be.getPos() != null && renderManager.hitResult != null && renderManager.hitResult.getPos() != null && renderManager.hitResult.getPos().equals(be.getPos()))
		{
			method_3570(true); //disabling lightmap
			method_3567(be, levelsText.getFormattedText(), x, y, z, 12); //drawing the nameplate
			method_3570(false); //enabling lightmap
		}


		double offset = Math.sin((be.getWorld().getTime() + partialTicks) * bobSpeed / 8.0D) / 10.0D; //TODO: config option to change bobSpeed
		BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(emerald, be.getWorld(), null);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.enableBlend();
		GuiLighting.enable();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.pushMatrix();
		GlStateManager.translated(x + 0.5D, y + 0.5D + offset, z + 0.5D);
		GlStateManager.scalef(0.75F, 0.75F, 0.75F);
		GlStateManager.rotatef((be.getWorld().getTime() + partialTicks) * 4.0F * spinSpeed, 0.0F, 1.0F, 0.0F); //TODO: config option to change spinSpeed
		MinecraftClient.getInstance().getItemRenderer().renderItemAndGlow(emerald, model);
		GlStateManager.popMatrix();
		GuiLighting.disable();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
}

package bl4ckscor3.mod.globalxp.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.tileentity.XPBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.StringTextComponent;

public class XPBlockTileEntityRenderer extends TileEntityRenderer<XPBlockTileEntity>
{
	private ItemStack emerald = new ItemStack(Items.EMERALD, 1);

	public XPBlockTileEntityRenderer()
	{
		super(TileEntityRendererDispatcher.instance);
	}

	@Override
	public void func_225616_a_(XPBlockTileEntity te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int p_225616_5_, int p_225616_6_)
	{
		//TODO: fix nameplate rendering
		if(Configuration.CONFIG.renderNameplate.get())
		{
			RayTraceResult rtr = field_228858_b_.cameraHitResult;

			if(te != null && te.getPos() != null && rtr != null && rtr.getType() == Type.BLOCK && ((BlockRayTraceResult)rtr).getPos() != null && ((BlockRayTraceResult)rtr).getPos().equals(te.getPos()))
			{
				String levelsString = new StringTextComponent((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")").getFormattedText();
				float opacity = Minecraft.getInstance().gameSettings.func_216840_a(0.25F); //backgroundOpacity or something like that
				int j = (int)(opacity * 255.0F) << 24;
				FontRenderer fontRenderer = field_228858_b_.fontRenderer;
				float halfWidth = -fontRenderer.getStringWidth(levelsString) / 2;
				Matrix4f matrix4f;

				stack.func_227860_a_(); //push
				stack.func_227861_a_(0.5D, 1.5D, 0.5D); //translate
				stack.func_227863_a_(Minecraft.getInstance().getRenderManager().func_229098_b_()); //rotate
				stack.func_227862_a_(-0.025F, -0.025F, 0.025F); //scale
				matrix4f = stack.func_227866_c_().func_227870_a_();
				fontRenderer.func_228079_a_(levelsString, halfWidth, 0, 553648127, false, matrix4f, buffer, true, j, p_225616_5_);
				fontRenderer.func_228079_a_(levelsString, halfWidth, 0, -1, false, matrix4f, buffer, false, 0, p_225616_5_);
				stack.func_227865_b_(); //pop
			}
		}

		float time = te.getWorld().getWorldInfo().getGameTime() + partialTicks;
		double offset = Math.sin(time * Configuration.CONFIG.bobSpeed.get() / 8.0D) / 10.0D;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(emerald, te.getWorld(), null);

		stack.func_227861_a_(0.5D, 0.4D + offset, 0.5D); //translate
		stack.func_227863_a_(new Quaternion(Vector3f.field_229181_d_, time * 4.0F * Configuration.CONFIG.spinSpeed.get().floatValue(), true)); //rotate, Y_AXIS
		Minecraft.getInstance().getItemRenderer().func_229111_a_(emerald, TransformType.GROUND, false, stack, buffer, p_225616_5_, p_225616_6_, model);
	}
}

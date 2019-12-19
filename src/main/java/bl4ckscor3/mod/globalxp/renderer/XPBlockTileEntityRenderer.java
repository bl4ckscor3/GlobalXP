package bl4ckscor3.mod.globalxp.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import bl4ckscor3.mod.globalxp.Configuration;
import bl4ckscor3.mod.globalxp.tileentity.XPBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
		//		if(Configuration.CONFIG.renderNameplate.get())
		//		{
		//			ITextComponent levelsText = new StringTextComponent((int)te.getStoredLevels() + " (" + te.getStoredXP() + ")");
		//			RayTraceResult rtr = field_228858_b_.cameraHitResult;
		//
		//			if(te != null && te.getPos() != null && rtr != null && rtr.getType() == Type.BLOCK && ((BlockRayTraceResult)rtr).getPos() != null && ((BlockRayTraceResult)rtr).getPos().equals(te.getPos()))
		//			{
		//				setLightmapDisabled(true);
		//				drawNameplate(te, levelsText.getFormattedText(), x, y, z, 12);
		//				setLightmapDisabled(false);
		//			}
		//		}

		float time = te.getWorld().getWorldInfo().getGameTime() + partialTicks;
		double offset = Math.sin(time * Configuration.CONFIG.bobSpeed.get() / 8.0D) / 10.0D;
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(emerald, te.getWorld(), null);

		stack.func_227861_a_(0.5D, 0.4D + offset, 0.5D); //translate
		stack.func_227863_a_(new Quaternion(Vector3f.field_229181_d_, time * 4.0F * Configuration.CONFIG.spinSpeed.get().floatValue(), true)); //rotate, Y_AXIS
		Minecraft.getInstance().getItemRenderer().func_229111_a_(emerald, TransformType.GROUND, false, stack, buffer, p_225616_5_, p_225616_6_, model);
	}
}

package bl4ckscor3.mod.globalxp.imc.waila;

import java.util.List;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@WailaPlugin(GlobalXP.MOD_ID)
public class WailaDataProvider implements IWailaPlugin, IComponentProvider
{
	public static final ResourceLocation XP_BLOCK = new ResourceLocation(GlobalXP.MOD_ID, "xp_block");
	public static final WailaDataProvider INSTANCE = new WailaDataProvider();

	@Override
	public void register(IRegistrar registrar)
	{
		registrar.registerComponentProvider(INSTANCE, TooltipPosition.BODY, XPBlock.class);
		registrar.registerStackProvider(INSTANCE, XPBlock.class);
	}

	@Override
	public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
	{
		if(accessor.getTileEntity() instanceof TileEntityXPBlock)
		{
			TileEntityXPBlock te = ((TileEntityXPBlock)accessor.getTileEntity());

			tooltip.add(new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", te.getStoredLevels())));

			if(accessor.getPlayer().isSneaking())
				tooltip.add(new TranslationTextComponent("info.globalxp.xp", te.getStoredXP()));
		}
	}

	@Override
	public ItemStack getStack(IDataAccessor accessor, IPluginConfig config)
	{
		return new ItemStack(GlobalXP.xp_block);
	}
}

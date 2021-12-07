package bl4ckscor3.mod.globalxp.compat;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin(GlobalXP.MOD_ID)
public class WailaDataProvider implements IWailaPlugin, IComponentProvider
{
	public static final ResourceLocation XP_BLOCK = new ResourceLocation(GlobalXP.MOD_ID, "xp_block");
	public static final WailaDataProvider INSTANCE = new WailaDataProvider();

	@Override
	public void register(IRegistrar registrar)
	{
		registrar.registerComponentProvider(INSTANCE, TooltipPosition.BODY, XPBlock.class);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
	{
		if(accessor.getTooltipPosition() == TooltipPosition.BODY && accessor.getBlockEntity() instanceof XPBlockEntity xpBlock)
		{
			tooltip.add(new TranslatableComponent("info.globalxp.levels", String.format("%.2f", xpBlock.getStoredLevels())));

			if(accessor.getPlayer().isCrouching())
				tooltip.add(new TranslatableComponent("info.globalxp.xp", xpBlock.getStoredXP()));
		}
	}
}

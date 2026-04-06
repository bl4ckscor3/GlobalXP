package bl4ckscor3.mod.globalxp.compat;

import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaClientPlugin;

public final class WTHITDataProvider extends HUDOverlayModHandler implements IWailaClientPlugin, IBlockComponentProvider {
	@Override
	public void register(IClientRegistrar registrar) {
		registrar.body(this, XPBlock.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		addXPInfo(accessor.getBlockEntity(), accessor.getPlayer().isCrouching(), tooltip::addLine);
	}
}

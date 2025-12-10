package bl4ckscor3.mod.globalxp.compat;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(GlobalXP.MOD_ID)
public final class WailaDataProvider extends HUDOverlayModHandler implements IWailaPlugin, IBlockComponentProvider {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(this, XPBlock.class);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		addXPInfo(accessor.getBlockEntity(), accessor.getPlayer().isCrouching(), tooltip::add);
	}

	@Override
	public Identifier getUid() {
		return Identifier.fromNamespaceAndPath(GlobalXP.MOD_ID, "display");
	}
}

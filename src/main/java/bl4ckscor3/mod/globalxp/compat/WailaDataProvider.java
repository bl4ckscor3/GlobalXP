package bl4ckscor3.mod.globalxp.compat;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(GlobalXP.MOD_ID)
public class WailaDataProvider implements IWailaPlugin, IBlockComponentProvider
{
	public static final ResourceLocation XP_BLOCK = new ResourceLocation(GlobalXP.MOD_ID, "xp_block");
	public static final WailaDataProvider INSTANCE = new WailaDataProvider();

	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.registerBlockComponent(INSTANCE, XPBlock.class);
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
	{
		if(accessor.getBlockEntity() instanceof XPBlockEntity xpBlock)
		{
			tooltip.add(Component.translatable("info.globalxp.levels", String.format("%.2f", xpBlock.getStoredLevels())));

			if(accessor.getPlayer().isCrouching())
				tooltip.add(Component.translatable("info.globalxp.xp", xpBlock.getStoredXP()));
		}
	}

	@Override
	public ResourceLocation getUid()
	{
		return new ResourceLocation(GlobalXP.MOD_ID, "display");
	}
}

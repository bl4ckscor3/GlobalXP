package bl4ckscor3.mod.globalxp.imc.waila;

//@WailaPlugin(GlobalXP.MOD_ID)
public class WailaDataProvider// implements IWailaPlugin, IComponentProvider
{
	//	public static final ResourceLocation XP_BLOCK = new ResourceLocation(GlobalXP.MOD_ID, "xp_block");
	//	public static final WailaDataProvider INSTANCE = new WailaDataProvider();
	//
	//	@Override
	//	public void register(IRegistrar registrar)
	//	{
	//		registrar.registerComponentProvider(INSTANCE, TooltipPosition.BODY, XPBlock.class);
	//		registrar.registerStackProvider(INSTANCE, XPBlock.class);
	//	}
	//
	//	@Override
	//	public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
	//	{
	//		if(accessor.getTileEntity() instanceof TileEntityXPBlock)
	//		{
	//			TileEntityXPBlock te = ((TileEntityXPBlock)accessor.getTileEntity());
	//
	//			tooltip.add(new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", te.getStoredLevels())));
	//
	//			if(accessor.getPlayer().isSneaking())
	//				tooltip.add(new TranslationTextComponent("info.globalxp.xp", te.getStoredXP()));
	//		}
	//	}
	//
	//	@Override
	//	public ItemStack getStack(IDataAccessor accessor, IPluginConfig config)
	//	{
	//		return new ItemStack(GlobalXP.xp_block);
	//	}
}

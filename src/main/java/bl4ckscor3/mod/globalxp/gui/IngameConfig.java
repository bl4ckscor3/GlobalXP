package bl4ckscor3.mod.globalxp.gui;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class IngameConfig extends GuiConfig
{
	public IngameConfig(GuiScreen parent)
	{
		super(parent, new ConfigElement(GlobalXP.config.getCategory("options")).getChildElements(), GlobalXP.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(GlobalXP.config.toString()));
	}
}

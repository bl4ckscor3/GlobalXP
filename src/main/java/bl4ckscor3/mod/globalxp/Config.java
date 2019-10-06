package bl4ckscor3.mod.globalxp;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config extends Configuration
{
	public double spinSpeed;
	public double bobSpeed;
	public boolean renderNameplate;
	public int xpForComparator;

	/**
	 * Sets up this mod's config file
	 * @param suggestedFile The file as per {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}#getSuggestedConfigurationFile
	 */
	public Config(File suggestedFile)
	{
		super(suggestedFile);

		refresh();
	}

	/**
	 * Refreshes this mod's configuration
	 */
	public void refresh()
	{
		load();

		Property prop;

		prop = get("options", "spinspeed", 1.0D);
		prop.setLanguageKey("globalxp.config.spinspeed");
		spinSpeed = prop.getDouble(1.0D);

		prop = get("options", "bobspeed", 1.0D);
		prop.setLanguageKey("globalxp.config.bobspeed");
		bobSpeed = prop.getDouble(1.0D);

		prop = get("options", "renderNameplate", true);
		prop.setLanguageKey("globalxp.config.renderNameplate");
		renderNameplate = prop.getBoolean(true);

		prop = get("options", "xpForComparator", 1395 / 15);
		prop.setLanguageKey("globalxp.config.xpForComparator");
		xpForComparator = Math.max(0, prop.getInt(1395 / 15));

		if(hasChanged())
			save();
	}
}

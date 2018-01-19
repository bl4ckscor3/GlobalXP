package bl4ckscor3.mod.globalxp;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config extends Configuration
{
	public double spinSpeed;
	public double bobSpeed;
	public boolean levelvisibility;
	
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
		
		prop = get("options", "levelvisibility", true);
		prop.setLanguageKey("globalxp.config.levelvisibility");
		levelvisibility = prop.getBoolean(true);
		
		if(hasChanged())
			save();
	}
}

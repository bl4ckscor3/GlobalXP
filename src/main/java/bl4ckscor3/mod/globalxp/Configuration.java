package bl4ckscor3.mod.globalxp;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class Configuration
{
	public static final ForgeConfigSpec CONFIG_SPEC;
	public static final Configuration CONFIG;

	public DoubleValue spinSpeed;
	public DoubleValue bobSpeed;
	public BooleanValue renderNameplate;

	static
	{
		Pair<Configuration,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Configuration::new);

		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ForgeConfigSpec.Builder builder)
	{
		spinSpeed = builder
				.comment("globalxp.config.spinspeed.tooltip")
				.defineInRange("spinSpeed", 1.0D, 0.0D, Double.MAX_VALUE);
		bobSpeed = builder
				.comment("globalxp.config.bobspeed.tooltip")
				.defineInRange("bobSpeed", 1.0D, 0.0D, Double.MAX_VALUE);
		renderNameplate = builder
				.translation("globalxp.config.renderNameplate.tooltip")
				.define("renderNameplate", true);
	}
}

package bl4ckscor3.mod.globalxp;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Configuration
{
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec CONFIG_COMMON;
	public static final ForgeConfigSpec CONFIG_SPEC;
	public static final Configuration CONFIG;

	public static BooleanValue captureXP;
	public DoubleValue spinSpeed;
	public DoubleValue bobSpeed;
	public BooleanValue renderNameplate;
	public IntValue xpForComparator;

	static
	{
		// needs to be a configuration option for both Client and Server.
		captureXP = COMMON_BUILDER
				.comment("Whether the XP Block will pickup any XP orbs around it")
				.define("captureXP", true);
		CONFIG_COMMON = COMMON_BUILDER.build();

		Pair<Configuration,ForgeConfigSpec> specPair = CLIENT_BUILDER.configure(Configuration::new);
		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ForgeConfigSpec.Builder builder)
	{
		spinSpeed = builder
				.comment("How fast the emerald should spin (multiplier of the default speed)")
				.defineInRange("spinSpeed", 1.0D, 0.0D, Double.MAX_VALUE);
		bobSpeed = builder
				.comment("How fast the emerald should bob up and down (multiplier of the default speed)")
				.defineInRange("bobSpeed", 1.0D, 0.0D, Double.MAX_VALUE);
		renderNameplate = builder
				.comment("Whether info about the saved levels should be shown above the XP Block")
				.define("renderNameplate", true);
		xpForComparator = builder
				.comment("The amount of XP needed for the comparator to output a redstone signal of strength one. By default, the signal will be at full strength if the block has 30 levels stored.")
				.defineInRange("xpForComparator", 1395 / 15, 0, Integer.MAX_VALUE / 15);
	}
}

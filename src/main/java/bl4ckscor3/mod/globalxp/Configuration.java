package bl4ckscor3.mod.globalxp;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

//@formatter:off
@Config(name = GlobalXP.MOD_ID)
public class Configuration implements ConfigData {
	@Comment("How fast the emerald should spin (multiplier of the default speed)")
	public float spinSpeed = 1.0F;

	@Comment("How fast the emerald should bob up and down (multiplier of the default speed)")
	public double bobSpeed = 1.0D;

	@Comment("Whether info about the saved levels should be shown above the XP Block")
	public boolean renderNameplate = true;

	@Comment("The amount of XP needed for the comparator to output a redstone signal of strength one. By default, the signal will be at full strength if the block has 30 levels stored.")
	@BoundedDiscrete(max = 143_165_576) // Integer.MAX_VALUE / 15
	public int xpForComparator = 93; // 1395 / 15

	@Comment("Whether the XP Block will pickup any XP orbs around it")
	public boolean pickupXP = true;

	@Comment("The range in blocks around the XP Block in which XP orbs will be picked up")
	@BoundedDiscrete(max = 50)
	public double pickupRange = 3.0D;

	@Comment("Setting this to true will remove only as much XP from the block at a time as is needed for the player to reach their next level. Setting to false will retrieve all stored XP at once.")
	public boolean retriveUntilNextLevel = true;

	@Comment("Sets the amount of XP points that will be removed from the XP Block and added to the player's XP bar when the player interacts with the block\nIf this is set to anything other than -1, this setting will override the \"retrieve_until_next_level\" configuration setting.\nAs such, setting this to 0 will disable XP retrieval, and setting to -1 will make the XP Block ignore this setting.")
	@BoundedDiscrete(min = -1, max = Integer.MAX_VALUE)
	public int retrievalAmount = -1;

	@Comment("Setting this to true will store only as much XP from the player's XP bar until reaching the previous level, meaning only one level at maximum will be added to the block's storage at a time. Setting to false will store all the XP the player has.")
	public boolean storeUntilPreviousLevel = false;

	@Comment("Sets the amount of XP points that will be removed from the player's XP bar and stored in the XP Block when the player interacts with it.\nIf this is set to anything other than -1, this setting will override the \"store_until_previous_level\" configuration setting.\nAs such, setting this to 0 will disable adding XP to the block, and setting to -1 will make the XP Block ignore this setting.")
	@BoundedDiscrete(min = -1, max = Integer.MAX_VALUE)
	public int storingAmount = -1;

	@Comment("The percentage of XP that the XP Block will give back, as a sort of cost of using it.\nExample: If this config value is set to 0.75, and an XP Block has 100 XP stored, attempting to retrieve these 100 XP will give back 75 XP.\nNote: This will not be 100% accurate, as Minecraft's XP does not use decimals.")
	@BoundedDiscrete(max = 1)
	public double retrievalPercentage = 1.0D;

	@Comment("Setting this to true will remove XP from the block in XP orb form. This is useful if you want to use XP from the block for tools enchanted with Mending.\nThese XP orbs will not be picked back up by the XP Block, if \"pickupXP\" is true.")
	public boolean retrieveXPOrbs = false;
}

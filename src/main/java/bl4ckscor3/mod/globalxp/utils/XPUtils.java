package bl4ckscor3.mod.globalxp.utils;

public class XPUtils
{
	/**
	 * Calculates the amount of levels the given XP value represents
	 * @param storedXP The amount of XP to get the level amount of
	 * @return The amount of levels storedXP represents
	 */
	public static float calculateStoredLevels(int storedXP)
	{
		float storedLevels = 0.0F;
		int xp = storedXP;

		while(xp > 0)
		{
			int xpToNextLevel = getXPForLevel((int)storedLevels);

			if(xp < xpToNextLevel)
			{
				storedLevels += (float)xp / xpToNextLevel;
				break;
			}

			xp -= xpToNextLevel;
			storedLevels += 1.0F;
		}

		return storedLevels;
	}

	/**
	 * Gets the amount of XP required to go from level to the next level
	 * Formula copied from EntityPlayer.java
	 * @param level The level to get the XP for
	 * @return The amount of XP required to go from level to the next level
	 */
	public static int getXPForLevel(int level)
	{
		if(level >= 30)
			return 112 + (level - 30) * 9;
		return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
	}

}

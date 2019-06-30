package bl4ckscor3.mod.globalxp.util;

import openmods.utils.EnchantmentUtils;

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
			int xpToNextLevel = EnchantmentUtils.xpBarCap((int)storedLevels);

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
	 * Gets the amount of XP needed until reaching the next level
	 * @param currentXP The XP that the player already has
	 * @return
	 */
	public static int getXPToNextLevel(int currentXP)
	{
		int level = EnchantmentUtils.getLevelForExperience(currentXP);
		int nextLevelXP = EnchantmentUtils.getExperienceForLevel(level + 1);

		return nextLevelXP - currentXP;
	}
}

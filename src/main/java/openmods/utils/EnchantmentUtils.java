/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Open Mods
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package openmods.utils;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;

public class EnchantmentUtils {

	/**
	 * Be warned, minecraft doesn't update experienceTotal properly, so we have
	 * to do this.
	 *
	 * @param player
	 * @return
	 */
	public static int getPlayerXP(PlayerEntity player) {
		return (int)(EnchantmentUtils.getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static void addPlayerXP(PlayerEntity player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = EnchantmentUtils.getLevelForExperience(experience);
		int expForLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);
		player.experience = (experience - expForLevel) / (float)player.xpBarCap();
	}

	public static int xpBarCap(int level) {
		if (level >= 30)
			return 112 + (level - 30) * 9;

		if (level >= 15)
			return 37 + (level - 15) * 5;

		return 7 + level * 2;
	}

	private static int sum(int n, int a0, int d) {
		return n * (2 * a0 + (n - 1) * d) / 2;
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0) return 0;
		if (level <= 15) return sum(level, 7, 2);
		if (level <= 30) return 315 + sum(level - 15, 37, 5);
		return 1395 + sum(level - 30, 112, 9);
	}

	public static int getXpToNextLevel(int level) {
		int levelXP = EnchantmentUtils.getLevelForExperience(level);
		int nextXP = EnchantmentUtils.getExperienceForLevel(level + 1);
		return nextXP - levelXP;
	}

	public static int getLevelForExperience(int targetXp) {
		int level = 0;
		while (true) {
			final int xpToNextLevel = xpBarCap(level);
			if (targetXp < xpToNextLevel) return level;
			level++;
			targetXp -= xpToNextLevel;
		}
	}

	//bl4ckscor3: ForgeHooks#getEnchantPower is gone in 1.14
	//	public static float getPower(World world, BlockPos position) {
	//		float power = 0;
	//
	//		for (int deltaZ = -1; deltaZ <= 1; ++deltaZ) {
	//			for (int deltaX = -1; deltaX <= 1; ++deltaX) {
	//				if ((deltaZ != 0 || deltaX != 0)
	//						&& world.isAirBlock(position.add(deltaX, 0, deltaZ))
	//						&& world.isAirBlock(position.add(deltaX, 1, deltaZ))) {
	//					power += ForgeHooks.getEnchantPower(world, position.add(deltaX * 2, 0, deltaZ * 2));
	//					power += ForgeHooks.getEnchantPower(world, position.add(deltaX * 2, 1, deltaZ * 2));
	//					if (deltaX != 0 && deltaZ != 0) {
	//						power += ForgeHooks.getEnchantPower(world, position.add(deltaX * 2, 0, deltaZ));
	//						power += ForgeHooks.getEnchantPower(world, position.add(deltaX * 2, 1, deltaZ));
	//						power += ForgeHooks.getEnchantPower(world, position.add(deltaX, 0, deltaZ * 2));
	//						power += ForgeHooks.getEnchantPower(world, position.add(deltaX, 1, deltaZ * 2));
	//					}
	//				}
	//			}
	//		}
	//		return power;
	//	}

	public static void addAllBooks(Enchantment enchantment, List<ItemStack> items) {
		for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
			items.add(EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
	}
}
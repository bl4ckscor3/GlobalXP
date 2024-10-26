package bl4ckscor3.mod.globalxp.xpblock;

import java.util.List;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class XPBlockItem extends BlockItem {
	public XPBlockItem(Block block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		int storedXP = stack.getOrDefault(GlobalXP.STORED_XP, 0);

		if (storedXP == 0)
			addInfo(tooltip, "0", 0);
		else
			addInfo(tooltip, String.format("%.2f", XPUtils.calculateStoredLevels(storedXP)), storedXP);
	}

	public void addInfo(List<Component> tooltip, String storedLevels, int storedXP) {
		tooltip.add(Component.translatable("info.globalxp.levels", storedLevels).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("info.globalxp.xp", storedXP).withStyle(ChatFormatting.GRAY));
	}
}

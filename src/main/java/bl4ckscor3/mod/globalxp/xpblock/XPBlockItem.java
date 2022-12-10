package bl4ckscor3.mod.globalxp.xpblock;

import java.util.List;

import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class XPBlockItem extends BlockItem {
	private static final Style STYLE = Style.EMPTY.applyFormat(ChatFormatting.GRAY);

	public XPBlockItem(Block block) {
		super(block, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!stack.hasTag()) {
			tooltip.add(Component.translatable("info.globalxp.levels", 0).setStyle(STYLE));
			tooltip.add(Component.translatable("info.globalxp.xp", 0).setStyle(STYLE));
		}
		else {
			int storedXP = stack.getTag().getCompound("BlockEntityTag").getInt("stored_xp");

			tooltip.add(Component.translatable("info.globalxp.levels", String.format("%.2f", XPUtils.calculateStoredLevels(storedXP))).setStyle(STYLE));
			tooltip.add(Component.translatable("info.globalxp.xp", storedXP).setStyle(STYLE));
		}
	}
}

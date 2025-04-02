package bl4ckscor3.mod.globalxp.xpblock;

import java.util.function.Consumer;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class XPBlockItem extends BlockItem {
	public XPBlockItem(Block block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		int storedXP = stack.get(GlobalXP.STORED_XP);

		if (storedXP == 0)
			addInfo(tooltipAdder, "0", 0);
		else
			addInfo(tooltipAdder, String.format("%.2f", XPUtils.calculateStoredLevels(storedXP)), storedXP);
	}

	public void addInfo(Consumer<Component> tooltipAdder, String storedLevels, int storedXP) {
		tooltipAdder.accept(Component.translatable("info.globalxp.levels", storedLevels).withStyle(ChatFormatting.GRAY));
		tooltipAdder.accept(Component.translatable("info.globalxp.xp", storedXP).withStyle(ChatFormatting.GRAY));
	}
}

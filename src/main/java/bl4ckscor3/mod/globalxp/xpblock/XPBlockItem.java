package bl4ckscor3.mod.globalxp.xpblock;

import java.util.List;

import bl4ckscor3.mod.globalxp.XPUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class XPBlockItem extends BlockItem {
	private static final Style STYLE = Style.EMPTY.applyFormat(ChatFormatting.GRAY);

	public XPBlockItem(Block block) {
		super(block, new Item.Properties());
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!stack.hasTag())
			addInfo(tooltip, "0", 0);
		else {
			CompoundTag stackTag = stack.getTag();
			long storedXP;

			if (stackTag.contains(BLOCK_ENTITY_TAG))
				stackTag = stackTag.getCompound(BLOCK_ENTITY_TAG);

			storedXP = stackTag.getLong("stored_xp");
			addInfo(tooltip, String.format("%.2f", XPUtils.calculateStoredLevels(storedXP)), storedXP);
		}
	}

	public void addInfo(List<Component> tooltip, String storedLevels, long storedXP) {
		tooltip.add(Component.translatable("info.globalxp.levels", storedLevels).setStyle(STYLE));
		tooltip.add(Component.translatable("info.globalxp.xp", storedXP).setStyle(STYLE));
	}
}

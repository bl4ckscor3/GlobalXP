package bl4ckscor3.mod.globalxp.items;

import java.util.List;

import bl4ckscor3.mod.globalxp.util.XPUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class XPBlockItem extends BlockItem
{
	private static final Style STYLE = Style.EMPTY.applyFormatting(TextFormatting.GRAY);

	public XPBlockItem(Block block)
	{
		super(block, new Item.Properties().group(ItemGroup.MISC));
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if(!stack.hasTag())
		{
			tooltip.add(new TranslationTextComponent("info.globalxp.levels", 0).func_230530_a_(STYLE));
			tooltip.add(new TranslationTextComponent("info.globalxp.xp", 0).func_230530_a_(STYLE));
		}
		else
		{
			int storedXP = stack.getTag().getCompound("BlockEntityTag").getInt("stored_xp");

			tooltip.add(new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", XPUtils.calculateStoredLevels(storedXP))).func_230530_a_(STYLE)); //setStyle
			tooltip.add(new TranslationTextComponent("info.globalxp.xp", storedXP).func_230530_a_(STYLE));
		}
	}
}

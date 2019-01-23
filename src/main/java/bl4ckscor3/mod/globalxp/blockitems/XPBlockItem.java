package bl4ckscor3.mod.globalxp.blockitems;

import java.util.List;

import bl4ckscor3.mod.globalxp.utils.XPUtils;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipOptions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;

public class XPBlockItem extends BlockItem
{
	public XPBlockItem(Block block, Settings settings)
	{
		super(block, settings);
	}

	@Override
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> list, TooltipOptions tooltipOptions)
	{
		if(!stack.hasTag())
		{
			list.add(new TranslatableTextComponent("info.levels", 0));
			list.add(new TranslatableTextComponent("info.xp", 0));
		}
		else
		{
			int storedXP = stack.getTag().getCompound("BlockEntityTag").getInt("stored_xp");

			list.add(new TranslatableTextComponent("info.levels", String.format("%.2f", XPUtils.calculateStoredLevels(storedXP))));
			list.add(new TranslatableTextComponent("info.xp", storedXP));
		}
	}
}

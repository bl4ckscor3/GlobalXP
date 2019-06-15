package bl4ckscor3.mod.globalxp.itemblocks;

import java.util.List;

import bl4ckscor3.mod.globalxp.util.XPUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemBlockXPBlock extends BlockItem
{
	public ItemBlockXPBlock(Block block)
	{
		super(block, new Item.Properties().group(ItemGroup.MISC));
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if(!stack.hasTag())
		{
			tooltip.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("info.globalxp.levels", 0).getFormattedText()));
			tooltip.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("info.globalxp.xp", 0).getFormattedText()));
		}
		else
		{
			int storedXP = stack.getTag().getCompound("BlockEntityTag").getInt("stored_xp");

			tooltip.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("info.globalxp.levels", String.format("%.2f", XPUtils.calculateStoredLevels(storedXP))).getFormattedText()));
			tooltip.add(new StringTextComponent(TextFormatting.GRAY + new TranslationTextComponent("info.globalxp.xp", storedXP).getFormattedText()));
		}
	}
}

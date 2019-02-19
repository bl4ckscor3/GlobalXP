package bl4ckscor3.mod.globalxp.itemblocks;

import java.util.List;

import bl4ckscor3.mod.globalxp.util.XPUtils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockXPBlock extends ItemBlock
{
	public ItemBlockXPBlock(Block block)
	{
		super(block);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		if(!stack.hasTagCompound())
		{
			tooltip.add(I18n.format("info.levels", 0));
			tooltip.add(I18n.format("info.xp", 0));
		}
		else
		{
			int storedXP = stack.getTagCompound().getCompoundTag("BlockEntityTag").getInteger("stored_xp");

			tooltip.add(I18n.format("info.levels", String.format("%.2f", XPUtils.calculateStoredLevels(storedXP))));
			tooltip.add(I18n.format("info.xp", storedXP));
		}
	}
}

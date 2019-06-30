package bl4ckscor3.mod.globalxp.handlers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import bl4ckscor3.mod.globalxp.util.XPUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openmods.utils.EnchantmentUtils;

@EventBusSubscriber(modid=GlobalXP.MOD_ID)
public class EventHandler
{
	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event)
	{
		if(!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof XPBlock) || event.getHand() != EnumHand.MAIN_HAND)
			return;

		if(!event.getWorld().isRemote)
		{
			EntityPlayer player = event.getEntityPlayer();

			if(player.isSneaking()) //sneaking = add all player xp to the block
			{
				int playerXP = EnchantmentUtils.getPlayerXP(player);

				((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos())).addXP(playerXP);
				EnchantmentUtils.addPlayerXP(player, -playerXP); // set player xp to 0
			}
			else //not sneaking = remove exactly enough xp from the block to get player to the next level
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos()));
				int neededXP = XPUtils.getXPToNextLevel(EnchantmentUtils.getPlayerXP(player));
				int availableXP = te.removeXP(neededXP);

				EnchantmentUtils.addPlayerXP(player, availableXP);
			}
		}
	}

	@SubscribeEvent
	public static void onOnConfigChanged(OnConfigChangedEvent event) //yes, this is how i name my event listener methods
	{
		if(event.getModID().equals(GlobalXP.MOD_ID))
		{
			GlobalXP.config.save();
			GlobalXP.config.refresh();
		}
	}
}

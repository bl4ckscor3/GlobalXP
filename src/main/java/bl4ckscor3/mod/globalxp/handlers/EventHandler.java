package bl4ckscor3.mod.globalxp.handlers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{
	@SubscribeEvent
	public void onRightClickBlock(RightClickBlock event)
	{
		if(!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof XPBlock) || event.getHand() != EnumHand.MAIN_HAND)
			return;

		if(!event.getWorld().isRemote)
		{
			if(event.getEntityPlayer().isSneaking())
			{
				// Sneaking = add all player XP to the block

				((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos())).addXp(event.getEntityPlayer().experienceTotal);
				event.getEntityPlayer().addExperienceLevel(-event.getEntityPlayer().experienceLevel - 1); // set player XP to 0
			}
			else
			{
				// Not sneaking = remove exactly enough XP from the block to get player to the next level

				TileEntityXPBlock te = ((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos()));
				EntityPlayer player = event.getEntityPlayer();

				int neededXp = player.xpBarCap() - (int)player.experience;
				int availableXp = te.removeXp(neededXp);
				
				event.getEntityPlayer().addExperience(availableXp);
			}
		}
	}
	
	@SubscribeEvent
	public void onOnConfigChanged(OnConfigChangedEvent event) //yes, this is how i name my event listener methods
	{
		if(event.getModID().equals(GlobalXP.MOD_ID))
		{
			GlobalXP.config.save();
			GlobalXP.config.refresh();
		}
	}
}

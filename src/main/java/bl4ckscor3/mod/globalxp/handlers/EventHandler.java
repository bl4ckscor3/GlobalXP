package bl4ckscor3.mod.globalxp.handlers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
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
			if(event.getEntityPlayer().isSneaking()) //sneaking = add all player xp to the block
			{
				((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos())).addXP(event.getEntityPlayer().experienceTotal);
				event.getEntityPlayer().addExperienceLevel(-event.getEntityPlayer().experienceLevel - 1); // set player xp to 0
			}
			else //not sneaking = remove exactly enough xp from the block to get player to the next level
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos()));
				EntityPlayer player = event.getEntityPlayer();
				int neededXP = player.xpBarCap() - (int)player.experience;
				int availableXP = te.removeXP(neededXP);

				player.addExperience(availableXP);
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

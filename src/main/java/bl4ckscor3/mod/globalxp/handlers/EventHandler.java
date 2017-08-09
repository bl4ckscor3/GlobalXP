package bl4ckscor3.mod.globalxp.handlers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.blocks.XPBlock;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
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
			if(event.getEntityPlayer().isSneaking()) //add all levels to the block
			{
				((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos())).addLevel(event.getEntityPlayer().experienceLevel);
				event.getEntityPlayer().addExperienceLevel(-event.getEntityPlayer().experienceLevel);
			}
			else //remove one level from the block
			{
				TileEntityXPBlock te = ((TileEntityXPBlock)event.getWorld().getTileEntity(event.getPos()));
				
				if(te.getStoredLevels() == 0)
					return;
				
				te.removeLevel();
				event.getEntityPlayer().addExperienceLevel(1);
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

package bl4ckscor3.mod.globalxp.network;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.renderer.TileEntityXPBlockRenderer;
import bl4ckscor3.mod.globalxp.tileentity.TileEntityXPBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends ServerProxy
{
	@Override
	@SideOnly(Side.CLIENT)
	public void loadModels()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(GlobalXP.xp_block), 0, new ModelResourceLocation("globalxp:xp_block", "inventory"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXPBlock.class, new TileEntityXPBlockRenderer());
	}
}

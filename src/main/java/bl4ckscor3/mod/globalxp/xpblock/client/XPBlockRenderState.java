package bl4ckscor3.mod.globalxp.xpblock.client;

import org.joml.Quaternionfc;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.FormattedCharSequence;

public class XPBlockRenderState extends BlockEntityRenderState {
	public boolean lookingAtBlock;
	public boolean renderXPInfo;
	public FormattedCharSequence xpInfo;
	public boolean renderCustomName;
	public FormattedCharSequence customName;
	public double yOffset;
	public Quaternionfc rotation;
	public ItemStackRenderState emerald = new ItemStackRenderState();
}

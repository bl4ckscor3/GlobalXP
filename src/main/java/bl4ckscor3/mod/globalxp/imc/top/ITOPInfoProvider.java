package bl4ckscor3.mod.globalxp.imc.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface ITOPInfoProvider
{
	/**
	 * Used to retrieve info for TheOneProbe about a block in the world when looking at it
	 * @param mode Indicates what kind of information to display
	 * @param probeInfo Information to return to the probe
	 * @param player The player involved
	 * @param world The world involved
	 * @param blockState The block state of the block being looked at
	 * @param data Access information about where the probe hit the block
	 */
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data);
}

package bl4ckscor3.mod.globalxp.compat;

import java.util.function.Function;

import bl4ckscor3.mod.globalxp.GlobalXP;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class GetTheOneProbe implements Function<ITheOneProbe, Void>
{
	@Override
	public Void apply(ITheOneProbe theOneProbe)
	{
		theOneProbe.registerProvider(new IProbeInfoProvider() {
			@Override
			public String getID()
			{
				return GlobalXP.MOD_ID + ":default";
			}

			@Override
			public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
			{
				if(blockState.getBlock() instanceof ITOPInfoProvider)
					((ITOPInfoProvider)blockState.getBlock()).addProbeInfo(mode, probeInfo, player, world, blockState, data);
			}
		});
		return null;
	}
}
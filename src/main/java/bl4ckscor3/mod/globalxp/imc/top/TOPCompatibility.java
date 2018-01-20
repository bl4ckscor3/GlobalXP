package bl4ckscor3.mod.globalxp.imc.top;

import java.util.function.Function;

import javax.annotation.Nullable;

import bl4ckscor3.mod.globalxp.GlobalXP;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPCompatibility
{
	private static boolean registered;

	public static void register()
	{
		if(registered)
			return;

		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "bl4ckscor3.mod.globalxp.imc.top.TOPCompatibility$GetTheOneProbe");
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void>
	{
		public static ITheOneProbe probe;

		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe)
		{
			probe = theOneProbe;
			probe.registerProvider(new IProbeInfoProvider() {
				@Override
				public String getID()
				{
					return GlobalXP.MOD_ID + ":default";
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
				{
					if(blockState.getBlock() instanceof ITOPInfoProvider)
						((ITOPInfoProvider)blockState.getBlock()).addProbeInfo(mode, probeInfo, player, world, blockState, data);
				}
			});
			return null;
		}
	}
}
package bl4ckscor3.mod.globalxp.compat;

import java.util.function.Function;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockTileEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
			public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData data)
			{
				BlockEntity tile = level.getBlockEntity(data.getPos());

				if(tile instanceof XPBlockTileEntity te)
				{
					probeInfo.horizontal().text(new TranslatableComponent("info.globalxp.levels", String.format("%.2f", te.getStoredLevels())));

					if(mode == ProbeMode.EXTENDED)
						probeInfo.horizontal().text(new TranslatableComponent("info.globalxp.xp", te.getStoredXP()));
				}
			}
		});
		return null;
	}
}
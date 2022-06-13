package bl4ckscor3.mod.globalxp.compat;

import java.util.function.Function;

import bl4ckscor3.mod.globalxp.GlobalXP;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GetTheOneProbe implements Function<ITheOneProbe, Void>, IProbeInfoProvider
{
	@Override
	public Void apply(ITheOneProbe theOneProbe)
	{
		theOneProbe.registerProvider(this);
		return null;
	}

	@Override
	public ResourceLocation getID()
	{
		return new ResourceLocation(GlobalXP.MOD_ID, "default");
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData data)
	{
		if(level.getBlockEntity(data.getPos()) instanceof XPBlockEntity xpBlock)
		{
			probeInfo.horizontal().text(Component.translatable("info.globalxp.levels", String.format("%.2f", xpBlock.getStoredLevels())));

			if(mode == ProbeMode.EXTENDED)
				probeInfo.horizontal().text(Component.translatable("info.globalxp.xp", xpBlock.getStoredXP()));
		}
	}
}
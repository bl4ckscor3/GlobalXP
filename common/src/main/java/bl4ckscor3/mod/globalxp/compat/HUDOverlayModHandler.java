package bl4ckscor3.mod.globalxp.compat;

import java.util.function.Consumer;

import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HUDOverlayModHandler {
	/**
	 * Adds experience info to a mod which shows HUD overlay info for blocks
	 *
	 * @param be The block entity of the XP block being looked at
	 * @param showXPAsWell true if the overlay should show levels and XP, false if it should show only levels
	 * @param lineAdder A {@link java.util.function.Consumer} which is responsible for adding the XP info to the HUD overlay
	 */
	public void addXPInfo(BlockEntity be, boolean showXPAsWell, Consumer<Component> lineAdder) {
		if (be instanceof XPBlockEntity xpBlock) {
			lineAdder.accept(Component.translatable("info.globalxp.levels", String.format("%.2f", xpBlock.getStoredLevels())));

			if (showXPAsWell)
				lineAdder.accept(Component.translatable("info.globalxp.xp", xpBlock.getStoredXP()));
		}
	}
}

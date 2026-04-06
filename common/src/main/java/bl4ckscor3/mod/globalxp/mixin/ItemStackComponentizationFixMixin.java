package bl4ckscor3.mod.globalxp.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Dynamic;

import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;

/**
 * Makes sure the xp b lock is properly converted to using components and no old data is lost
 */
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
	@Inject(method = "fixItemStack", at = @At("TAIL"))
	private static void globalXP$fixXPBlockItemWithNewData(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, CallbackInfo ci) {
		if (itemStackData.is("globalxp:xp_block"))
			itemStackData.moveTagToComponent("stored_xp", "globalxp:xp");
	}

	@Inject(method = "fixBlockEntityTag", at = @At("TAIL"), cancellable = true)
	private static void globalXP$fixXPBlockItemWithOldData(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, String blockEntityType, CallbackInfoReturnable<Dynamic<?>> ci) {
		if (itemStackData.is("globalxp:xp_block")) {
			Optional<?> storedXP = dynamic.get("stored_xp").result();

			if (storedXP.isPresent()) {
				itemStackData.setComponent("globalxp:xp", (Dynamic<?>) storedXP.get());
				ci.setReturnValue(dynamic.remove("stored_xp"));
			}
		}
	}
}

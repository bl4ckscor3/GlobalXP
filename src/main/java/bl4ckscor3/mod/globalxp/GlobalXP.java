package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.blockentities.XPBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntityType;

public class GlobalXP implements ModInitializer {
	
	public static final String MODID = "globalxp";
	public static final String NAME = "Global XP";
	public static final String VERSION = "1.0.0";
	
	public static final BlockEntityType<XPBlockEntity> BLOCK_ENTITY_OWNABLE = BlockEntityType.Builder.create(XPBlockEntity::new).method_11034(null);

	@Override
	public void onInitialize() {
		
	}

}

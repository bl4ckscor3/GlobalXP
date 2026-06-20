package bl4ckscor3.mod.globalxp;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import bl4ckscor3.mod.globalxp.lib.Platform;
import bl4ckscor3.mod.globalxp.lib.RegisteredBlock;
import bl4ckscor3.mod.globalxp.lib.RegisteredItem;
import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GlobalXP {
	public static final String MODID = "globalxp";
	private static Platform platform;
	public static final RegisteredBlock<XPBlock> XP_BLOCK = RegisteredBlock.create("xp_block", XPBlock::new, () -> BlockBehaviour.Properties.of().strength(5.0F, 2000.0F).sound(SoundType.METAL));
	public static final Supplier<BlockEntityType<XPBlockEntity>> XP_BLOCK_ENTITY_TYPE = Suppliers.memoize(() -> platform.createBlockEntity(XPBlockEntity::new, XP_BLOCK.get()));
	public static final RegisteredItem<XPBlockItem> XP_BLOCK_ITEM = RegisteredItem.blockItem("xp_block", p -> new XPBlockItem(XP_BLOCK.get(), p.component(GlobalXP.STORED_XP.get(), 0)), Item.Properties::new);
	public static final Supplier<DataComponentType<Integer>> STORED_XP = Suppliers.memoize(() -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());

	public synchronized static void initialize(Platform platform) {
		if (GlobalXP.platform != null) {
			throw new IllegalArgumentException(MODID + " platform has already been initialized");
		}

		GlobalXP.platform = platform;
		platform.register(Registries.BLOCK, XP_BLOCK);
		platform.register(Registries.BLOCK_ENTITY_TYPE, XP_BLOCK_ENTITY_TYPE, "xp_block");
		platform.register(Registries.ITEM, XP_BLOCK_ITEM);
		platform.register(Registries.DATA_COMPONENT_TYPE, STORED_XP, "xp");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}

	public static Platform platform() {
		return platform;
	}
}

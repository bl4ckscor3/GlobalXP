package bl4ckscor3.mod.globalxp;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

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
	public static final RegistryObject<XPBlock> XP_BLOCK = RegistryObject.block("xp_block", XPBlock::new, () -> BlockBehaviour.Properties.of().strength(5.0F, 2000.0F).sound(SoundType.METAL));
	public static final Supplier<BlockEntityType<XPBlockEntity>> XP_BLOCK_ENTITY_TYPE = Suppliers.memoize(() -> platform.createBlockEntity(XPBlockEntity::new, XP_BLOCK.get()));
	public static final RegistryObject<XPBlockItem> XP_BLOCK_ITEM = RegistryObject.item("xp_block", p -> new XPBlockItem(XP_BLOCK.get(), p.component(GlobalXP.STORED_XP.get(), 0)), () -> new Item.Properties().useBlockDescriptionPrefix());
	public static final Supplier<DataComponentType<Integer>> STORED_XP = Suppliers.memoize(() -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());

	public synchronized static void initialize(Platform platform) {
		if (GlobalXP.platform != null) {
			throw new IllegalArgumentException(MODID + " platform has already been initialized");
		}

		GlobalXP.platform = platform;
		platform.register(Registries.BLOCK, XP_BLOCK);
		platform.register(Registries.BLOCK_ENTITY_TYPE, XP_BLOCK_ENTITY_TYPE, id("xp_block"));
		platform.register(Registries.ITEM, XP_BLOCK_ITEM);
		platform.register(Registries.DATA_COMPONENT_TYPE, STORED_XP, id("xp"));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}

	public static Platform platform() {
		return platform;
	}
}

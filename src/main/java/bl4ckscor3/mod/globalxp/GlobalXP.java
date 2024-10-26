package bl4ckscor3.mod.globalxp;

import bl4ckscor3.mod.globalxp.xpblock.XPBlock;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockEntity;
import bl4ckscor3.mod.globalxp.xpblock.XPBlockItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GlobalXP implements ModInitializer {
	public static final String MOD_ID = "globalxp";
	public static final ResourceLocation XP_BLOCK_NAME = ResourceLocation.fromNamespaceAndPath(MOD_ID, "xp_block");
	public static final XPBlock XP_BLOCK = new XPBlock(BlockBehaviour.Properties.of().strength(5F, 2000.0F).sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, XP_BLOCK_NAME)));
	public static final BlockEntityType<XPBlockEntity> XP_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder.create(XPBlockEntity::new, XP_BLOCK).build();
	public static final XPBlockItem XP_BLOCK_ITEM = new XPBlockItem(XP_BLOCK, new Item.Properties().component(GlobalXP.STORED_XP, 0).setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(GlobalXP.MOD_ID, "xp_block"))).useBlockDescriptionPrefix());
	public static final DataComponentType<Integer> STORED_XP = DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build();
	public static final Configuration CONFIG = AutoConfig.register(Configuration.class, JanksonConfigSerializer::new).getConfig();

	@Override
	public void onInitialize() {
		Registry.register(BuiltInRegistries.BLOCK, XP_BLOCK_NAME, XP_BLOCK);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, XP_BLOCK_NAME, XP_BLOCK_ENTITY_TYPE);
		Registry.register(BuiltInRegistries.ITEM, XP_BLOCK_NAME, XP_BLOCK_ITEM);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "xp"), STORED_XP);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.accept(XP_BLOCK_ITEM));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(XP_BLOCK_ITEM));
	}
}

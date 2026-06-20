package bl4ckscor3.mod.globalxp;

import java.util.Optional;
import java.util.function.Supplier;

import bl4ckscor3.mod.globalxp.lib.Platform;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.config.ModConfig;

public class FabricEntrypoint implements ModInitializer, Platform {
	@Override
	public void onInitialize() {
		GlobalXP.initialize(this);
		ConfigRegistry.INSTANCE.register(GlobalXP.MODID, ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
		ConfigRegistry.INSTANCE.register(GlobalXP.MODID, ModConfig.Type.SERVER, Configuration.SERVER_SPEC);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> output.accept(new ItemStack(GlobalXP.XP_BLOCK_ITEM.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(output -> output.accept(new ItemStack(GlobalXP.XP_BLOCK_ITEM.get()), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY));
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock) {
		return FabricBlockEntityTypeBuilder.create(factory::create, validBlock).build();
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, String path) {
		Optional<Holder.Reference<R>> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey);

		if (registry.isEmpty()) {
			throw new IllegalArgumentException("Couldn't find registry " + registryKey);
		}

		Registry.register((Registry<R>) registry.get().value(), GlobalXP.id(path), entry.get());
	}
}

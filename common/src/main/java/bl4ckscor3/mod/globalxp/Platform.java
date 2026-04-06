package bl4ckscor3.mod.globalxp;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface Platform {
	<R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, Identifier id);

	default <R, T extends R> void register(ResourceKey<? extends Registry<R>> registry, RegistryObject<T> registryObject) {
		register(registry, registryObject.object(), registryObject.id());
	}

	<T extends BlockEntity> BlockEntityType<T> createBlockEntity(BlockEntityFactory<T> factory, Block validBlock);

	default void onXpChange(Player player, int amount) {
	}

	default void onLevelChange(Player player, int experienceLevel) {
	}

	@FunctionalInterface
	interface BlockEntityFactory<T extends BlockEntity> {
		T create(BlockPos blockPos, BlockState blockState);
	}
}

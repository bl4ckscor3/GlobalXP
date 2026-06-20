package bl4ckscor3.mod.globalxp.lib;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record RegisteredBlock<T extends Block>(ResourceKey<Block> key, Supplier<T> object) implements RegistryObject<Block, T> {
	public static <B extends Block> RegisteredBlock<B> create(String id, BlockConstructor<B> blockConstructor, Supplier<BlockBehaviour.Properties> properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, GlobalXP.id(id));
		return new RegisteredBlock<>(
			key,
			Suppliers.memoize(() -> blockConstructor.construct(properties.get().setId(key)))
		);
	}

	@FunctionalInterface
	public interface BlockConstructor<B extends Block> {
		B construct(BlockBehaviour.Properties properties);
	}
}

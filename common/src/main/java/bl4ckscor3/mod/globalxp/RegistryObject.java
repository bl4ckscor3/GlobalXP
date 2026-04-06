package bl4ckscor3.mod.globalxp;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record RegistryObject<T>(Identifier id, Supplier<T> object) {
	public static <B extends Block> RegistryObject<B> block(String id, BlockConstructor<B> blockConstructor, Supplier<BlockBehaviour.Properties> properties) {
		Identifier key = GlobalXP.id(id);
		return new RegistryObject<>(
			key,
			Suppliers.memoize(() -> blockConstructor.construct(properties.get().setId(ResourceKey.create(Registries.BLOCK, key))))
		);
	}

	public static <I extends Item> RegistryObject<I> item(String id, ItemConstructor<I> itemConstructor, Supplier<Item.Properties> properties) {
		Identifier key = GlobalXP.id(id);
		return new RegistryObject<>(
			key,
			Suppliers.memoize(() -> itemConstructor.construct(properties.get().setId(ResourceKey.create(Registries.ITEM, key))))
		);
	}

	public T get() {
		return object.get();
	}

	@FunctionalInterface
	public interface BlockConstructor<B extends Block> {
		B construct(BlockBehaviour.Properties properties);
	}

	@FunctionalInterface
	public interface ItemConstructor<I extends Item> {
		I construct(Item.Properties properties);
	}
}

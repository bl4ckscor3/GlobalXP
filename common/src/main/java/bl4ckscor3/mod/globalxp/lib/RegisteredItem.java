package bl4ckscor3.mod.globalxp.lib;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public record RegisteredItem<T extends Item>(ResourceKey<Item> key, Supplier<T> object) implements RegistryObject<Item, T> {
	public static <I extends Item> RegisteredItem<I> item(Identifier id, ItemConstructor<I> itemConstructor, Supplier<Item.Properties> properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		return new RegisteredItem<>(
			key,
			Suppliers.memoize(() -> itemConstructor.construct(properties.get().setId(key)))
		);
	}

	public static <I extends Item> RegisteredItem<I> item(String id, ItemConstructor<I> itemConstructor, Supplier<Item.Properties> properties) {
		return item(GlobalXP.id(id), itemConstructor, properties);
	}

	public static <I extends BlockItem> RegisteredItem<I> blockItem(String id, ItemConstructor<I> itemConstructor, Supplier<Item.Properties> properties) {
		return item(id, itemConstructor, () -> properties.get().useBlockDescriptionPrefix());
	}

	@FunctionalInterface
	public interface ItemConstructor<I extends Item> {
		I construct(Item.Properties properties);
	}
}

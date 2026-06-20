package bl4ckscor3.mod.globalxp.lib;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceKey;

public interface RegistryObject<RegistryType, ObjectType extends RegistryType> {
	ResourceKey<RegistryType> key();

	Supplier<ObjectType> object();

	default ObjectType get() {
		return object().get();
	}
}

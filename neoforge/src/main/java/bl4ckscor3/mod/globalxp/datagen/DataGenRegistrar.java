package bl4ckscor3.mod.globalxp.datagen;

import java.util.List;
import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class DataGenRegistrar {
	private DataGenRegistrar() {}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent.Client event) {
		event.createProvider(BlockTagGenerator::new);
		event.createReloadableRegistryObjects(
			new RegistrySetBuilder()
				.add(RecipeProvider.asBootstrap(RecipeGenerator::new))
				.add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(new SubProviderEntry(NeoBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)))),
			Set.of(GlobalXP.MODID)
		);
	}
}

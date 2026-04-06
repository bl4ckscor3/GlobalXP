package bl4ckscor3.mod.globalxp.datagen;

import java.util.List;
import java.util.Set;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
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
		event.createProvider((output, lookupProvider) -> new LootTableProvider(output, Set.of(), List.of(new SubProviderEntry(NeoBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
		event.createProvider(RecipeGenerator.Runner::new);
	}
}

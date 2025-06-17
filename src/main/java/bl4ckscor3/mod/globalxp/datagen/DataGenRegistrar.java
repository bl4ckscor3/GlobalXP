package bl4ckscor3.mod.globalxp.datagen;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.DetectedVersion;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent.DataProviderFromOutputLookup;

@EventBusSubscriber(modid = GlobalXP.MOD_ID, bus = Bus.MOD)
public class DataGenRegistrar {
	private DataGenRegistrar() {}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent.Client event) {
		event.createProvider(BlockTagGenerator::new);
		event.createProvider((DataProviderFromOutputLookup<LootTableProvider>) (output, lookupProvider) -> new LootTableProvider(output, Set.of(), List.of(new SubProviderEntry(BlockLootTableGenerator::new, LootContextParamSets.BLOCK)), lookupProvider));
		event.createProvider(RecipeGenerator.Runner::new);
		//@formatter:off
		event.createProvider(output -> new PackMetadataGenerator(output)
				.add(PackMetadataSection.TYPE, new PackMetadataSection(Component.literal("Global XP resources & data"),
						DetectedVersion.BUILT_IN.packVersion(PackType.CLIENT_RESOURCES),
						Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
		//@formatter:on
	}
}

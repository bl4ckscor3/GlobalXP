package bl4ckscor3.mod.globalxp.datagen;

import java.util.List;
import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class NeoBlockLootTableSubProvider extends BlockLootSubProvider {
	protected NeoBlockLootTableSubProvider(LootTableSubProvider.Context output) {
		super(Set.of(GlobalXP.XP_BLOCK.get().asItem()), FeatureFlags.REGISTRY.allFlags(), output);
	}

	@Override
	public void generate() {
		BlockLootTableGenerator.generate(this::add, this::applyExplosionCondition);
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return List.of(GlobalXP.XP_BLOCK.get());
	}
}

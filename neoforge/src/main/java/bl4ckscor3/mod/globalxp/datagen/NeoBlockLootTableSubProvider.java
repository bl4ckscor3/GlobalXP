package bl4ckscor3.mod.globalxp.datagen;

import java.util.List;
import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class NeoBlockLootTableSubProvider extends BlockLootSubProvider {
	protected NeoBlockLootTableSubProvider(HolderLookup.Provider lookupProvider) {
		super(Set.of(GlobalXP.XP_BLOCK.get().asItem()), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
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

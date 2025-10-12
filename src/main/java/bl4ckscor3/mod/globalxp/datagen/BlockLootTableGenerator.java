package bl4ckscor3.mod.globalxp.datagen;

import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockLootTableGenerator extends BlockLootSubProvider {
	protected BlockLootTableGenerator(HolderLookup.Provider lookupProvider) {
		super(Set.of(GlobalXP.XP_BLOCK.get().asItem()), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
	}

	@Override
	public void generate() {
		add(GlobalXP.XP_BLOCK.get(), this::createXPBlockDrop);
	}

	private LootTable.Builder createXPBlockDrop(Block xpBlock) {
		//@formatter:off
        return LootTable.lootTable().withPool(applyExplosionCondition(xpBlock, LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(xpBlock)
						.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
								.include(DataComponents.CUSTOM_NAME)
								.include(GlobalXP.STORED_XP.get())))));
		//@formatter:on
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return (Iterable<Block>) GlobalXP.BLOCKS.getEntries().stream().map(DeferredHolder::get).toList();
	}
}

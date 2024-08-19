package bl4ckscor3.mod.globalxp.datagen;

import java.util.Set;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class BlockLootTableGenerator extends BlockLootSubProvider {
	protected BlockLootTableGenerator() {
		super(Set.of(GlobalXP.XP_BLOCK.get().asItem()), FeatureFlags.REGISTRY.allFlags());
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
						.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
						.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
								.copy("stored_xp", "stored_xp")))));
		//@formatter:on
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return GlobalXP.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
	}
}

package bl4ckscor3.mod.globalxp.datagen;

import java.util.function.Function;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BlockLootTableGenerator {
	public static void generate(LootTableAdder adder, ExplosionConditionApplier applier) {
		adder.add(GlobalXP.XP_BLOCK.get(), block -> createXPBlockDrop(block, applier));
	}

	private static LootTable.Builder createXPBlockDrop(Block xpBlock, ExplosionConditionApplier applier) {
		//@formatter:off
        return LootTable.lootTable().withPool(applier.applyExplosionCondition(xpBlock, LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(xpBlock)
						.apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
								.include(DataComponents.CUSTOM_NAME)
								.include(GlobalXP.STORED_XP.get())))));
		//@formatter:on
	}

	@FunctionalInterface
	public interface LootTableAdder {
		void add(Block block, Function<Block, LootTable.Builder> builder);
	}

	@FunctionalInterface
	public interface ExplosionConditionApplier {
		<T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder);
	}
}

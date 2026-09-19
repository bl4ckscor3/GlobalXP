package bl4ckscor3.mod.globalxp.datagen;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeGenerator extends RecipeProvider {
	private static final TagKey<Item> GEMS_EMERALD = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "gems/emerald"));
	private final HolderGetter<Item> items;

	public RecipeGenerator(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
		super(recipeOutput, advancementOutput);
		items = recipeOutput.lookup(Registries.ITEM);
	}

	@Override
	public final void buildRecipes() {
		ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, GlobalXP.XP_BLOCK.get())
			.pattern("BBB")
			.pattern("BEB")
			.pattern("BBB")
			.define('B', Items.IRON_BARS)
			.define('E', GEMS_EMERALD)
			.unlockedBy("has_emerald", has(GEMS_EMERALD))
			.save(output);
	}
}

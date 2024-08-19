package bl4ckscor3.mod.globalxp.datagen;

import java.util.function.Consumer;

import bl4ckscor3.mod.globalxp.GlobalXP;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected final void buildRecipes(Consumer<FinishedRecipe> consumer) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GlobalXP.XP_BLOCK.get())
		.pattern("BBB")
		.pattern("BEB")
		.pattern("BBB")
		.define('B', Items.IRON_BARS)
		.define('E', Tags.Items.GEMS_EMERALD)
		.unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
		.save(consumer);
		//@formatter:on
	}
}

package io.github.mclovelock.lovelock.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.mclovelock.lovelock.Lovelock;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class WarmingCabinetRecipe implements Recipe<SimpleContainer> {

	public static class Type implements RecipeType<WarmingCabinetRecipe> {
		public static final Type INSTANCE = new Type();

		public static final String ID = "warming";

		private Type() {
		}
	}

	public static class Serializer implements RecipeSerializer<WarmingCabinetRecipe> {
		public static final Serializer INSTANCE = new Serializer();

		public static final ResourceLocation ID = new ResourceLocation(Lovelock.MODID, Type.ID);

		@SuppressWarnings("unchecked")
		private static <T> Class<T> genericCast(Class<?> clazz) {
			return (Class<T>)clazz;
		}
		
		private Serializer() {
		}

		@Override
		public WarmingCabinetRecipe fromJson(ResourceLocation id, JsonObject json) {
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int maxProgress = json.get("max_progress").getAsInt();
			
			JsonArray ing = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> ingredients = NonNullList.withSize(ing.size(), Ingredient.EMPTY);
			
			for (int i = 0; i < ingredients.size(); i++) {
				ingredients.set(i, Ingredient.fromJson(ing.get(i)));
			}
			
			return new WarmingCabinetRecipe(id, result, ingredients, maxProgress);
		}
		
		@Override
		public WarmingCabinetRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			NonNullList<Ingredient> ingredients = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
			int maxProgress = buffer.readInt();
			
			for (int i = 0; i < ingredients.size(); i++) {
				ingredients.set(i, Ingredient.fromNetwork(buffer));
			}
			
			ItemStack result = buffer.readItem();
			return new WarmingCabinetRecipe(id, result, ingredients, maxProgress);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WarmingCabinetRecipe recipe) {
			buffer.writeInt(recipe.getIngredients().size());
			buffer.writeInt(recipe.getMaxProgress());
			
			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}
			
			buffer.writeItemStack(recipe.getResultItem(), false);
		}
		
		@Override
		public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
			return INSTANCE;
		}
		
		@Override
		public ResourceLocation getRegistryName() {
			return ID;
		}
		
		@Override
		public Class<RecipeSerializer<?>> getRegistryType() {
			return genericCast(RecipeSerializer.class);
		}
	}

	private final ResourceLocation id;
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients;
	private final int maxProgress;
	
	public WarmingCabinetRecipe(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients, int maxProgress) {
		this.id = id;
		this.result = result;
		this.ingredients = ingredients;
		this.maxProgress = maxProgress;
	}

	/*
	 * This system works as follows:
	 * In WarmingCabinetBlockEntity level.getRecipeManager().getRecipeFor(...) is called.
	 * The RecipeManager invokes this matches method. As we parse a SimpleContainer to the RecipeManager, which it in turn
	 * passes on to this matches method, we can assume the contents.
	 * 
	 * To account for several recipe productions running simultaneously in the BlockEntity, the SimpleContainer has the following structure:
	 * It only contains, at the very first index, the contents of the respective input slot in the WarmingCabinetBlockEntity.
	 * Now we can easily check if this singular recipe exists, and as we receive, as a result, merely a boolean weather this recipe is valid, we apply it
	 * to the slot in question.
	 * 
	 * For good measure, at the second index (1) the container also holds the contents of the specific slot's output. However this is never used.
	 */
	@Override
	public boolean matches(SimpleContainer container, Level level) {
		return ingredients.get(0).test(container.getItem(0)); // we only have one ingredient per recipe, i.e. the first one in the array.
	}

	@Override
	public ItemStack assemble(SimpleContainer container) {
		return result;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return result.copy();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}
	
	public int getMaxProgress() {
		return maxProgress;
	}

}

package io.github.mclovelock.lovelock.core.init;

import java.util.function.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.recipe.WarmingCabinetRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class RecipeInit {

	// Recipe Registration
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, Lovelock.MODID);

	public static void register(IEventBus eventBus) {
		RECIPE_SERIALIZERS.register(eventBus);
	}

	private static <T extends Recipe<SimpleContainer>> RegistryObject<RecipeSerializer<T>> registerRecipe(String verb,
			Supplier<RecipeSerializer<T>> serializerSupplier) {
		return RECIPE_SERIALIZERS.register(verb, serializerSupplier);
	}

	private static <T extends Recipe<SimpleContainer>> RegistryObject<RecipeSerializer<T>> registerRecipe(String verb,
			RecipeSerializer<T> serializer) {
		return registerRecipe(verb, () -> serializer);
	}

	// RECIPES

	public static final RegistryObject<RecipeSerializer<WarmingCabinetRecipe>> WARMING_CABINET_RECIPE_SERIALIZER = registerRecipe(
			"warming", WarmingCabinetRecipe.Serializer.INSTANCE);

	// don't ever initialise this class.
	private RecipeInit() {
	}

}

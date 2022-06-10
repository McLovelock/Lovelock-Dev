package io.github.mclovelock.lovelock.core.init;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.recipe.WarmingCabinetRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, Lovelock.MODID);

	// Init

	public static final RegistryObject<RecipeSerializer<WarmingCabinetRecipe>> WARMING_CABINET_SERIALIZER = SERIALIZERS
			.register("warming", () -> WarmingCabinetRecipe.Serializer.INSTANCE);

	public static void register(IEventBus eventBus) {
		SERIALIZERS.register(eventBus);
	}

}

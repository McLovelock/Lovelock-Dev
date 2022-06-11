package io.github.mclovelock.lovelock.core.event;

import javax.annotation.Nonnull;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.recipe.WarmingCabinetRecipe;
import io.github.mclovelock.lovelock.core.event.loot.TrichodermaReeseiFromGrassAdditionModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Lovelock.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

	@SubscribeEvent
	public static void registerModifierSerializers(
			@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		System.out.println("Registering modifiers!");
		event.getRegistry().registerAll(new TrichodermaReeseiFromGrassAdditionModifier.Serializer()
				.setRegistryName(new ResourceLocation(Lovelock.MODID, "trichoderma_reesei_from_grass")));
	}
	
	@SubscribeEvent
	public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
		System.out.println("registering recipes!");
		Registry.register(Registry.RECIPE_TYPE, WarmingCabinetRecipe.Type.ID, WarmingCabinetRecipe.Type.INSTANCE);
	}
	
}

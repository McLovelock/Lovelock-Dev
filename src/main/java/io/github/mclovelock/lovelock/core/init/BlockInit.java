package io.github.mclovelock.lovelock.core.init;

import java.util.function.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.client.CreativeTabs;
import io.github.mclovelock.lovelock.common.block.WarmingCabinetBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockInit {

	// Block Registration

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Lovelock.MODID);

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
		return ItemInit.ITEMS.register(name,
				() -> new BlockItem(block.get(), ItemInit.root().tab(CreativeTabs.LOVELOCK)));
	}

	private static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> block) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	// BLOCKS

	public static final RegistryObject<Block> WARMING_CABINET = registerBlock("warming_cabinet",
			() -> new WarmingCabinetBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY)
					.requiresCorrectToolForDrops()));

	// don't every initialise this class!
	private BlockInit() {
	}

}
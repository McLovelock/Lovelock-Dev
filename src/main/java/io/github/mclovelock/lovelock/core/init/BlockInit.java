package io.github.mclovelock.lovelock.core.init;

import java.util.function.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.client.CreativeTabs;
import io.github.mclovelock.lovelock.common.block.custom.WarmingCabinetBlock;
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

public class BlockInit {

	public static final Item.Properties root() {
		return new Item.Properties();
	}

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Lovelock.MODID);

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
		return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), root().tab(CreativeTabs.LOVELOCK)));
	}

	private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	// Blocks

	// BlockWithoutItem

	public static final RegistryObject<Block> WARMING_CABINET = registerBlockWithoutBlockItem("warming_cabinet",
			() -> new WarmingCabinetBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY)
					.requiresCorrectToolForDrops()));

	public static final RegistryObject<Item> WARMING_CABINET1 = registerBlockItem("warming_cabinet", WARMING_CABINET);

	// Registry

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

}

package io.github.mclovelock.lovelock.core.init;

import com.google.common.base.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.client.CreativeTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {

	// Item Registration

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lovelock.MODID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static final Item.Properties root() {
		return new Item.Properties();
	}

	private static RegistryObject<Item> registerItem(String name, Supplier<? extends Item> supplier) {
		return ITEMS.register(name, supplier);
	}

	private static RegistryObject<Item> registerItem(String name, Item.Properties properties) {
		return registerItem(name, () -> new Item(properties));
	}

	private static RegistryObject<Item> registerBlockItem(String name, Supplier<? extends BlockItem> supplier) {
		return ITEMS.register(name, supplier);
	}

	private static RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block,
			Item.Properties properties) {
		return registerBlockItem(name, () -> new BlockItem(block.get(), properties));
	}

	// ITEMS

	public static final RegistryObject<Item> SULPHUR_CRYSTAL = registerItem("sulphur_crystal",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> LAB_BOTTLE = registerItem("lab_bottle", root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PDA_LAB_BOTTLE = registerItem("pda_lab_bottle",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> AGER_AGER = registerItem("agar_agar", root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> CARROT_JUICE_BOTTLE = registerItem("carrot_juice_bottle",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PETRI_DISH = registerItem("petri_dish", root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PDA_PETRI_DISH = registerItem("pda_petri_dish",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> CARROT_JUICE_PDA_PETRI_DISH = registerItem("carrot_juice_pda_petri_dish",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> TRICHODERMA_REESEI = registerItem("trichoderma_reesei",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> TRICHODERMA_REESEI_PETRI_DISH = registerItem(
			"trichoderma_reesei_petri_dish", root().tab(CreativeTabs.LOVELOCK));

	// BLOCK ITEMS

	public static final RegistryObject<Item> WARMING_CABINET_ITEM = registerBlockItem("warming_cabinet",
			BlockInit.WARMING_CABINET, root().tab(CreativeTabs.LOVELOCK));

	// don't ever initialise this class!
	private ItemInit() {
	}

}

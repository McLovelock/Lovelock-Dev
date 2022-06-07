package io.github.mclovelock.lovelock.core.init;

import com.google.common.base.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.client.CreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lovelock.MODID);

	public static final Item.Properties root() {
		return new Item.Properties();
	}

	private static RegistryObject<Item> RegisterItem(String name, Item.Properties properties) {
		return RegisterItem(name, () -> new Item(properties));
	}

	private static RegistryObject<Item> RegisterItem(String name, Supplier<? extends Item> supplier) {
		return ITEMS.register(name, supplier);
	}

	public static final RegistryObject<Item> SULPHUR_CRYSTAL = RegisterItem("sulphur_crystal",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> LAB_BOTTLE = RegisterItem("lab_bottle",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PDA_LAB_BOTTLE = RegisterItem("pda_lab_bottle",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> AGER_AGER = RegisterItem("agar_agar",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> CARROT_JUICE_BOTTLE = RegisterItem("carrot_juice_bottle",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PETRI_DISH = RegisterItem("petri_dish",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> PDA_PETRI_DISH = RegisterItem("pda_petri_dish",
			root().tab(CreativeTabs.LOVELOCK));
	public static final RegistryObject<Item> CARROT_JUICE_PDA_PETRI_DISH = RegisterItem("carrot_juice_pda_petri_dish",
			root().tab(CreativeTabs.LOVELOCK));		
	public static final RegistryObject<Item> TRICHODERMA_REESEI = RegisterItem("trichoderma_reesei",
			root().tab(CreativeTabs.LOVELOCK));		
	public static final RegistryObject<Item> TRICHODERMA_REESEI_PETRI_DISH = RegisterItem("trichoderma_reesei_petri_dish",
			root().tab(CreativeTabs.LOVELOCK));	
	

	
	
	//Registry
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
	
}

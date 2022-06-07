package io.github.mclovelock.lovelock.core.init;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.screen.WarmingCabinetMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {

	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
			Lovelock.MODID);

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
			IContainerFactory<T> pFactory, String pName) {
		return MENUS.register(pName, () -> IForgeMenuType.create(pFactory));
	}

	// Init

	public static final RegistryObject<MenuType<WarmingCabinetMenu>> WARMING_CABINET_MENU = registerMenuType(
			WarmingCabinetMenu::new, "warming_cabinet_menu");

	// Registry

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}

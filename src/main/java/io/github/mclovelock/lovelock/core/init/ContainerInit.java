package io.github.mclovelock.lovelock.core.init;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.container.WarmingCabinetContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ContainerInit {

	// Container-Menu Registration

	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
			Lovelock.MODID);

	public static void register(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
	}

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuContainer(String name,
			Supplier<MenuType<T>> menuTypeSupplier) {
		return CONTAINERS.register(name, menuTypeSupplier);
	}

	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuContainer(String name,
			Class<T> clazz) {
		MenuSupplier<T> constructor = (id, playerInventory) -> {
			try {
				Constructor<T> classConstructor = clazz.getConstructor(int.class, Inventory.class);
				return (T) classConstructor.newInstance(id, playerInventory);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		};
		return registerMenuContainer(name, () -> new MenuType<>(constructor));
	}

	// CONTAINER-MENUS

	public static final RegistryObject<MenuType<WarmingCabinetContainer>> WARMING_CABINET = registerMenuContainer(
			"warming_cabinet", WarmingCabinetContainer.class);

	// don't ever initialise this class
	private ContainerInit() {
	}

}

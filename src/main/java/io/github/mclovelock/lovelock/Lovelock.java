package io.github.mclovelock.lovelock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import io.github.mclovelock.lovelock.core.init.BlockInit;
import io.github.mclovelock.lovelock.core.init.ItemInit;
import io.github.mclovelock.lovelock.core.init.MenuInit;
import io.github.mclovelock.lovelock.screen.WarmingCabinetScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Lovelock.MODID)
public class Lovelock {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "lovelock";

	public Lovelock() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::clientSetup);
		
		ItemInit.register(bus);
		BlockInit.register(bus);
		BlockEntityInit.register(bus);
		MenuInit.register(bus);
		
		
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
			
	}
	private void clientSetup(final FMLCommonSetupEvent event) {	
		MenuScreens.register(MenuInit.WARMING_CABINET_MENU.get(), WarmingCabinetScreen::new);
	}
	

}

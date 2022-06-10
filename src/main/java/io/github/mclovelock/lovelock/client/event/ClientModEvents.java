package io.github.mclovelock.lovelock.client.event;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.client.screens.WarmingCabinetScreen;
import io.github.mclovelock.lovelock.core.init.ContainerInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Lovelock.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(ContainerInit.WARMING_CABINET.get(), WarmingCabinetScreen::new);
	}
	
	// don't ever initialise this class!
	private ClientModEvents() {	}
	
}

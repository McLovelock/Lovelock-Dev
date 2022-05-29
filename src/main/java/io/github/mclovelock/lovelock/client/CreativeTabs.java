package io.github.mclovelock.lovelock.client;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.core.init.BlockInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class CreativeTabs {

	public static final CreativeModeTab LOVELOCK = new CreativeModeTab(Lovelock.MODID) {
		@Override
		public ItemStack makeIcon() {
			return BlockInit.ELECTROLYSER_ITEM.get().getDefaultInstance();
		}
	};
}

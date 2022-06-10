package io.github.mclovelock.lovelock.common.block.entity;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.container.WarmingCabinetContainer;
import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import io.github.mclovelock.lovelock.core.init.ItemInit;
import io.github.mclovelock.lovelock.core.util.tick.ImplementedBlockEntityTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class WarmingCabinetBlockEntity extends AbstractInventoryBlockEntity implements ImplementedBlockEntityTicker {

	public static final Component TITLE = new TranslatableComponent("container." + Lovelock.MODID + ".warming_cabinet");

	public WarmingCabinetBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WARMING_CABINET.get(), pos, state, WarmingCabinetContainer.N_ITEM_SLOTS);
	}

	private void craftItem(int slot) {
		int startIndex = slot;
		int completeIndex = slot + WarmingCabinetContainer.SLOT_COUNT;

		inventory.extractItem(startIndex, 1, false);
		inventory.setStackInSlot(completeIndex, new ItemStack(ItemInit.TRICHODERMA_REESEI_PETRI_DISH.get(),
				inventory.getStackInSlot(completeIndex).getCount() + 1));
	}

	private boolean hasRecipe(int index) {
		return inventory.getStackInSlot(index).getItem() == ItemInit.CARROT_JUICE_PDA_PETRI_DISH.get();
	}

	private boolean hasNotReachedStackLimit(int index) {
		return inventory.getStackInSlot(index).getCount() < inventory.getStackInSlot(index).getMaxStackSize();
	}

	public void tick() {
		for (int slot = 0; slot < WarmingCabinetContainer.SLOT_COUNT; slot++) {
			int startIndex = slot;
			int completeIndex = slot + WarmingCabinetContainer.SLOT_COUNT;

			if (hasRecipe(startIndex) && hasNotReachedStackLimit(completeIndex)) {
				System.out.println("Crafting at slot " + slot + ".");
				craftItem(slot);
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
	}

	@Override
	public void drops() {
		SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			inventory.setItem(i, this.inventory.getStackInSlot(i));
		}
		Containers.dropContents(level, worldPosition, inventory);
	}

}

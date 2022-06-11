package io.github.mclovelock.lovelock.common.block.entity;

import java.util.Optional;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.recipe.WarmingCabinetRecipe;
import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import io.github.mclovelock.lovelock.core.init.ItemInit;
import io.github.mclovelock.lovelock.core.util.tick.ImplementedBlockEntityTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

public class WarmingCabinetBlockEntity extends AbstractInventoryBlockEntity implements ImplementedBlockEntityTicker {

	public final class Data implements ContainerData {
		public static final int COUNT = 2 * WarmingCabinetBlockEntity.SLOT_COUNT; // The only data to be transmitted
																					// here is the progress data.

		@Override
		public void set(int index, int value) {
			if ((index >= 0) && (index < SLOT_COUNT)) { // negative indices means getting from the progress array
				progresses[index] = value;
			} else if ((index >= SLOT_COUNT * 1) && (index < SLOT_COUNT * 2)) {
				maxProgresses[index - SLOT_COUNT * 1] = value;
			}

			throw new IndexOutOfBoundsException(index);
		}

		@Override
		public int get(int index) {
			if ((index >= 0) && (index < progresses.length)) { // negative indices means getting from the progress
																// array
				return progresses[index];
			} else if ((index >= SLOT_COUNT * 1) && (index < SLOT_COUNT * 2)) {
				return maxProgresses[index - SLOT_COUNT * 1];
			}

			throw new IndexOutOfBoundsException(index);
		}

		@Override
		public int getCount() {
			return COUNT;
		}
	}

	public static final Component TITLE = new TranslatableComponent("container." + Lovelock.MODID + ".warming_cabinet");

	public static final int SLOT_COUNT = 5;
	public static final int N_ITEM_SLOTS = 2 * SLOT_COUNT;

	public final ContainerData data;

	// Save the progress of each simulations production going on, as well as its
	// respective maxProgress for different productions taking different amounts of
	// time.
	private final int[] maxProgresses = new int[SLOT_COUNT];
	private final int[] progresses = new int[SLOT_COUNT];

	public WarmingCabinetBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WARMING_CABINET.get(), pos, state, N_ITEM_SLOTS);

		data = new Data();

		// testing code
		for (int i = 0; i < SLOT_COUNT; i++) {
			maxProgresses[i] = 40;
		}
	}

	private void resetSlot(int slot) {
		progresses[slot] = 0;
	}

	private void craftItem(int slot) {
		int startIndex = slot;
		int completeIndex = slot + SLOT_COUNT;

		SimpleContainer inventory = new SimpleContainer(2);
		inventory.setItem(0, super.inventory.getStackInSlot(startIndex));
		inventory.setItem(1, super.inventory.getStackInSlot(completeIndex));

		Optional<WarmingCabinetRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(WarmingCabinetRecipe.Type.INSTANCE, inventory, level);

		if (recipe.isPresent()) {
			super.inventory.extractItem(startIndex, 1, false);
			super.inventory.setStackInSlot(completeIndex, new ItemStack(recipe.get().getResultItem().getItem(),
					super.inventory.getStackInSlot(completeIndex).getCount() + 1));
		}
	}

	private boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, int index) {
		return inventory.getItem(index).getMaxStackSize() > inventory.getItem(index).getCount();
	}

	private boolean canInsertResultItemIntoOutputSlot(SimpleContainer inventory, ItemStack resultItem, int index) {
		return (inventory.getItem(index).getItem() == resultItem.getItem()) || inventory.getItem(index).isEmpty();
	}

	private boolean hasRecipe(int index) {
		int startIndex = index;
		int completeIndex = index + SLOT_COUNT;

		SimpleContainer inventory = new SimpleContainer(2);
		inventory.setItem(0, super.inventory.getStackInSlot(startIndex));
		inventory.setItem(1, super.inventory.getStackInSlot(completeIndex));

		Optional<WarmingCabinetRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(WarmingCabinetRecipe.Type.INSTANCE, inventory, level);

		if (recipe.isPresent()) {
			return canInsertAmountIntoOutputSlot(inventory, completeIndex)
					&& canInsertResultItemIntoOutputSlot(inventory, recipe.get().getResultItem(), completeIndex);
		}

		return false;
	}

	public void tick() {
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			if ((super.inventory.getStackInSlot(0).getItem() == ItemInit.CARROT_JUICE_PDA_PETRI_DISH.get())
					&& (super.inventory.getStackInSlot(0).getCount() > 0)) {
			}
			if (hasRecipe(slot)) {
				progresses[slot]++;
				setChanged();
				if (progresses[slot] > maxProgresses[slot]) {
					craftItem(slot);
				}
			} else {
				resetSlot(slot);
				setChanged();
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			nbt.putInt("progress_" + String.valueOf(slot), progresses[slot]);
			nbt.putInt("max_progress_" + String.valueOf(slot), maxProgresses[slot]);
		}
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			progresses[slot] = nbt.getInt("progress_" + String.valueOf(slot));
			maxProgresses[slot] = nbt.getInt("max_progress_" + String.valueOf(slot));
		}
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

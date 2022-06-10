package io.github.mclovelock.lovelock.common.container;

import io.github.mclovelock.lovelock.common.block.entity.WarmingCabinetBlockEntity;
import io.github.mclovelock.lovelock.core.init.BlockInit;
import io.github.mclovelock.lovelock.core.init.ContainerInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class WarmingCabinetContainer extends AbstractContainerMenu {

	public static final int SLOT_COUNT = 5;
	public static final int N_ITEM_SLOTS = 2 * SLOT_COUNT;

	private final ContainerLevelAccess containerAccess;

	// Server constructor
	public WarmingCabinetContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos) {
		super(ContainerInit.WARMING_CABINET.get(), id);
		this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);

		initialiseContainerInventorySlots(slots);
		initialisePlayerInventorySlots(playerInventory);
	}

	private void initialisePlayerInventorySlots(Inventory playerInventory) {
		final int slotSizePlus2 = 18;
		final int startX = 8, startY = 84;
		final int hotbarY = 142;

		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				addSlot(new Slot(playerInventory, 9 + row * 9 + column, startX + column * slotSizePlus2,
						startY + row * slotSizePlus2));
			}
		}
		for (int column = 0; column < 9; column++) {
			addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}
	}

	private void initialiseContainerInventorySlots(IItemHandler slots) {
		final int slotWidthPlusDistance = 28;
		final int startX = 24;
		final int startYTop = 18, startYBottom = 54;

		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			addSlot(new SlotItemHandler(slots, slot +          0, startX + slot * slotWidthPlusDistance, startYTop));
		}
		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			addSlot(new SlotItemHandler(slots, slot + SLOT_COUNT, startX + slot * slotWidthPlusDistance, startYBottom));
		}
	}

	// Client constructor
	public WarmingCabinetContainer(int id, Inventory playerInventory) {
		this(id, playerInventory, new ItemStackHandler(N_ITEM_SLOTS), BlockPos.ZERO);
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(containerAccess, player, BlockInit.WARMING_CABINET.get());
	}

	public static MenuConstructor getServerContainer(WarmingCabinetBlockEntity warmingCabinet, BlockPos pos) {
		return (id, playerInventory, player) -> new WarmingCabinetContainer(id, playerInventory,
				warmingCabinet.inventory, pos);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		var retStack = ItemStack.EMPTY;
		final Slot slot = getSlot(index);
		if (slot.hasItem()) {
			final ItemStack item = slot.getItem();
			retStack = item.copy();
			if (index < N_ITEM_SLOTS) {
				if (!moveItemStackTo(item, N_ITEM_SLOTS, slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!moveItemStackTo(item, 0, N_ITEM_SLOTS, false)) {
				return ItemStack.EMPTY;
			}
			if (item.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return retStack;
	}

}

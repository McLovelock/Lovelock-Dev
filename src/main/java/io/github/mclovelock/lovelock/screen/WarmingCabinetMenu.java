package io.github.mclovelock.lovelock.screen;

import io.github.mclovelock.lovelock.common.block.entity.custom.WarmingCabinetBlockEntity;
import io.github.mclovelock.lovelock.core.init.BlockInit;
import io.github.mclovelock.lovelock.core.init.MenuInit;
import io.github.mclovelock.lovelock.screen.slot.ModResultSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class WarmingCabinetMenu extends AbstractContainerMenu {

	private final WarmingCabinetBlockEntity blockEntity;
	private final Level level;
	private final ContainerData data;
	
	
	public WarmingCabinetMenu(int pContainerId, Inventory pInv, FriendlyByteBuf pExtraData) {
		this(pContainerId, pInv, pInv.player.level.getBlockEntity(pExtraData.readBlockPos()), new SimpleContainerData(WarmingCabinetBlockEntity.rtx));

	}

	public WarmingCabinetMenu(int pContainerId, Inventory pInv, BlockEntity pEntity, ContainerData pData) {
		super(MenuInit.WARMING_CABINET_MENU.get(), pContainerId);
		checkContainerSize(pInv, TE_INVENTORY_SLOT_COUNT);
		blockEntity = ((WarmingCabinetBlockEntity) pEntity);
		this.level = pInv.player.level;
		this.data = pData;
		
		addPlayerInventory(pInv);
		addPlayerHotbar(pInv);

		this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			this.addSlot(new SlotItemHandler(handler, 0, 24, 18));
			this.addSlot(new SlotItemHandler(handler, 1, 52, 18));
			this.addSlot(new SlotItemHandler(handler, 2, 80, 18));
			this.addSlot(new SlotItemHandler(handler, 3, 108, 18));
			this.addSlot(new SlotItemHandler(handler, 4, 136, 18));
			this.addSlot(new ModResultSlot(handler, 5, 24, 54));
			this.addSlot(new ModResultSlot(handler, 6, 52, 54));
			this.addSlot(new ModResultSlot(handler, 7, 80, 54));
			this.addSlot(new ModResultSlot(handler, 8, 108, 54));
			this.addSlot(new ModResultSlot(handler, 9, 136, 54));			
		});
		addDataSlots(data);
	}

	public boolean isCrafting(int index) {
        return data.get(index) > 0;
    }

    public int getScaledProgress(int index) {
        int progress = this.data.get(index);
        int maxProgress = WarmingCabinetBlockEntity.CRAFTING_PROGRESS;  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
	
	
	
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	// THIS YOU HAVE TO DEFINE!
	public static final int TE_INVENTORY_SLOT_COUNT = 10; // must be the number of slots you have!

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot sourceSlot = slots.get(index);
		if (sourceSlot == null || !sourceSlot.hasItem())
			return ItemStack.EMPTY; // EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX,
					TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY; // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
					false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.out.println("Invalid slotIndex:" + index);
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(playerIn, sourceStack);
		return copyOfSourceStack;

	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer,
				BlockInit.WARMING_CABINET.get());
	}

	private void addPlayerInventory(Inventory playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
			}
		}
	}

	private void addPlayerHotbar(Inventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
}

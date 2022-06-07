package io.github.mclovelock.lovelock.common.block.entity.custom;

import java.util.Random;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import io.github.mclovelock.lovelock.core.init.ItemInit;
import io.github.mclovelock.lovelock.screen.WarmingCabinetMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class WarmingCabinetBlockEntity extends BlockEntity implements MenuProvider {

	private final ItemStackHandler itemHandler = new ItemStackHandler(WarmingCabinetMenu.TE_INVENTORY_SLOT_COUNT) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		};
	};

	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public WarmingCabinetBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntityInit.WARMING_CABINET_BLOCK_ENTITY.get(), pPos, pBlockState);

	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new WarmingCabinetMenu(pContainerId, pInventory, this);
	}

	@Override
	public Component getDisplayName() {

		return new TextComponent("Warming Cabinet");
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItemHandler.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.put("inventory", itemHandler.serializeNBT());
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
	}

	public static void tick(Level pLevel, BlockPos pPos, BlockState pState, WarmingCabinetBlockEntity pBlockEntity) {
		if (hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity)) {
			craftItem(pBlockEntity);
		}
	}

	private static void craftItem(WarmingCabinetBlockEntity pBlockEntity) {
		pBlockEntity.itemHandler.extractItem(0, 1, false);
		pBlockEntity.itemHandler.extractItem(1, 1, false);

		pBlockEntity.itemHandler.setStackInSlot(5, new ItemStack(ItemInit.TRICHODERMA_REESEI_PETRI_DISH.get(),
				pBlockEntity.itemHandler.getStackInSlot(5).getCount() + 1));
	}

	private static boolean hasRecipe(WarmingCabinetBlockEntity pBlockEntity) {
		boolean hasItemInFirstSlot = pBlockEntity.itemHandler.getStackInSlot(0).getItem() == ItemInit.PDA_LAB_BOTTLE
				.get();
		boolean hasItemInSecondSlot = pBlockEntity.itemHandler.getStackInSlot(1)
				.getItem() == ItemInit.CARROT_JUICE_PDA_PETRI_DISH.get();

		return hasItemInFirstSlot && hasItemInSecondSlot;
	}

	private static boolean hasNotReachedStackLimit(WarmingCabinetBlockEntity pBlockEntity) {
		return pBlockEntity.itemHandler.getStackInSlot(5).getCount() < pBlockEntity.itemHandler.getStackInSlot(5)
				.getMaxStackSize();
	}

}

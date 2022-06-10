package io.github.mclovelock.lovelock.common.block.entity.custom;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import io.github.mclovelock.lovelock.core.init.ItemInit;
import io.github.mclovelock.lovelock.recipe.WarmingCabinetRecipe;
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
import net.minecraft.world.inventory.ContainerData;
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

	private final ContainerData data;
	private int[] progress = new int[rtx];
	public static final int CRAFTING_PROGRESS = 72;
	public static int rtx = 5;

	public WarmingCabinetBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(BlockEntityInit.WARMING_CABINET_BLOCK_ENTITY.get(), pPos, pBlockState);
		this.data = new ContainerData() {
			public int get(int index) {
				return progress[index];
			}

			public void set(int index, int value) {
				progress[index] = value;
			}

			public int getCount() {
				return rtx;
			}
		};
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new WarmingCabinetMenu(pContainerId, pInventory, this, this.data);
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
		for (int i = 0; i < 4; i++) {
			tag.putInt("warming_cabinet.progress_" + i, progress[i]);
		}
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		// progress = nbt.getInt("warming_cabinet.progress");
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
	}

	public static void tick(Level pLevel, BlockPos pPos, BlockState pState, WarmingCabinetBlockEntity pBlockEntity) {
		for (int i = 0; i < 4; i++) {
			if (hasRecipe(pBlockEntity, i)) {
				pBlockEntity.progress[i]++;
				setChanged(pLevel, pPos, pState);
				if (pBlockEntity.progress[i] > CRAFTING_PROGRESS) {
					craftItem(pBlockEntity, i);

				}
			} else {
				pBlockEntity.resetProgress(i);
				setChanged(pLevel, pPos, pState);
			}
		}
	}

	private static boolean hasRecipe(WarmingCabinetBlockEntity entity, int index) {
		Level level = entity.level;
		SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
		for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
			inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
		}

		Optional<WarmingCabinetRecipe> match = level.getRecipeManager().getRecipeFor(WarmingCabinetRecipe.Type.INSTANCE,
				inventory, level);

		if (!match.isPresent()) {
			return false;
		}
		
		match.get().setIndex(index);
		if (index == 3) {
			System.out.println(match.get().getResultItem());
		}
		return match.isPresent() && canInsertAmountIntoOutputSlot(inventory, index)
				&& canInsertItemIntoOutputSlot(inventory, match.get().getResultItem(), index);

	}

	private static void craftItem(WarmingCabinetBlockEntity entity, int index) {

		Level level = entity.level;
		SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
		for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
			inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
		}

		Optional<WarmingCabinetRecipe> match = level.getRecipeManager().getRecipeFor(WarmingCabinetRecipe.Type.INSTANCE,
				inventory, level);

		if (match.isPresent()) {
			match.get().setIndex(index);
			entity.itemHandler.extractItem(index, 1, false);

			entity.itemHandler.setStackInSlot(index + 5, new ItemStack(match.get().getResultItem().getItem(),
					entity.itemHandler.getStackInSlot(5).getCount() + 1));

			entity.resetProgress(index);

		}
	}

	private void resetProgress(int index) {
		this.progress[index] = 0;
	}

	private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output, int index) {
		return inventory.getItem(index + 5).getItem() == output.getItem() || inventory.getItem(index + 5).isEmpty();
	}

	private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, int index) {
		return inventory.getItem(index + 5).getMaxStackSize() > inventory.getItem(index + 5).getCount();
	}
}

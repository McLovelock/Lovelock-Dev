package io.github.mclovelock.lovelock.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractInventoryBlockEntity extends BlockEntity {

	public final int size;
	public final ItemStackHandler inventory;

	protected int timer;
	protected boolean requiresUpdate;
	protected LazyOptional<ItemStackHandler> handler;

	public AbstractInventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
		super(type, pos, state);
		this.size = size;

		this.inventory = createInventory();
		this.handler = LazyOptional.of(() -> inventory);
	}
	
	protected abstract void drops();

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(size) {
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				AbstractInventoryBlockEntity.this.update();
				return super.extractItem(slot, amount, simulate);
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				AbstractInventoryBlockEntity.this.update();
				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	public void update() {
		requestModelDataUpdate();
		setChanged();
		if (level != null) {
			level.setBlockAndUpdate(worldPosition, getBlockState());
		}
	}

	public void tick() {
		timer++;
		if (requiresUpdate && (level != null)) {
			update();
			requiresUpdate = false;
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.handler.cast()
				: super.getCapability(cap, side);
	}

	public ItemStack getItemInSlot(int slot) {
		return handler.map(inv -> inv.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
	}

	public ItemStack extractItem(int slot) {
		final int count = getItemInSlot(slot).getCount();
		requiresUpdate = true;
		return handler.map(inv -> inv.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
	}

	public ItemStack insertItem(int slot, ItemStack stack) {
		ItemStack copy = stack.copy();
		stack.shrink(copy.getCount());
		requiresUpdate = true;
		return handler.map(inv -> inv.insertItem(slot, copy, false)).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		load(tag);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getTag());
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("inventory", inventory.serializeNBT());
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		inventory.deserializeNBT(tag.getCompound("inventory"));
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		handler.invalidate();
	}

	// GETTERS AND SETTERS

	public LazyOptional<ItemStackHandler> getHandler() {
		return handler;
	}

}

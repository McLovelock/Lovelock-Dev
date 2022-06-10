package io.github.mclovelock.lovelock.core.init;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.block.entity.WarmingCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityInit {

	// BlockEntity Registration
	
	public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Lovelock.MODID);

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name,
			Supplier<BlockEntityType<T>> blockEntitySupplier) {
		return BLOCK_ENTITIES.register(name, blockEntitySupplier);
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name,
			Class<T> clazz, RegistryObject<? extends Block> block) {
		BlockEntitySupplier<T> constructor = (pos, state) -> {
			try {
				Constructor<T> classConstructor = clazz.getConstructor(BlockPos.class, BlockState.class);
				return (T) classConstructor.newInstance(pos, state);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		};
		return registerBlockEntity(name, () -> BlockEntityType.Builder.of(constructor, block.get()).build(null));
	}

	// BLOCK ENTITIES

	public static RegistryObject<BlockEntityType<WarmingCabinetBlockEntity>> WARMING_CABINET = registerBlockEntity(
			"warming_cabinet", WarmingCabinetBlockEntity.class, BlockInit.WARMING_CABINET);

	// don't ever initialise this class!
	private BlockEntityInit() {
	}

}
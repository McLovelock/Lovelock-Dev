package io.github.mclovelock.lovelock.core.init;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.block.entity.custom.WarmingCabinetBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Lovelock.MODID);	

	public static final RegistryObject<BlockEntityType<WarmingCabinetBlockEntity>> WARMING_CABINET_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("warming_cabinet_block_entity", () ->
			BlockEntityType.Builder.of(WarmingCabinetBlockEntity::new, 
					BlockInit.WARMING_CABINET.get()).build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
	

}



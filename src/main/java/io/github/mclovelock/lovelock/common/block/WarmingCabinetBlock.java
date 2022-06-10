package io.github.mclovelock.lovelock.common.block;

import io.github.mclovelock.lovelock.common.block.entity.WarmingCabinetBlockEntity;
import io.github.mclovelock.lovelock.common.container.WarmingCabinetContainer;
import io.github.mclovelock.lovelock.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class WarmingCabinetBlock extends BaseEntityBlock implements EntityBlock {

	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public WarmingCabinetBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (oldState.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof WarmingCabinetBlockEntity) {
				((WarmingCabinetBlockEntity) blockEntity).drops();
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult result) {
		if (!level.isClientSide && (level.getBlockEntity(pos) instanceof final WarmingCabinetBlockEntity cabinet)) {
			MenuProvider container = new SimpleMenuProvider(WarmingCabinetContainer.getServerContainer(cabinet, pos),
					WarmingCabinetBlockEntity.TITLE);
			NetworkHooks.openGui((ServerPlayer) player, container, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityInit.WARMING_CABINET.get().create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> types) {
		return level.isClientSide ? null
				: ($0, $1, $2, blockEntity) -> ((WarmingCabinetBlockEntity) blockEntity).tick();
	}

}

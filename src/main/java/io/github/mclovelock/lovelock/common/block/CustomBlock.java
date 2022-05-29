package io.github.mclovelock.lovelock.common.block;


import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class CustomBlock extends HorizontalDirectionalBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

		public CustomBlock(Properties builder) {
			super(builder);
			this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		}
		public BlockState getStateForPlacement(BlockPlaceContext ctx) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		}
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(FACING);
		}
	}
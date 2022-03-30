package com.teamabnormals.pet_cemetery.common.block;

import com.teamabnormals.pet_cemetery.common.block.state.properties.CoilType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class CompanionCoilBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<CoilType> COIL_TYPE = EnumProperty.create("type", CoilType.class);
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 10);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	private static final VoxelShape TOP_AABB = Shapes.or(box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D), box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D));
	private static final VoxelShape MIDDLE_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

	public CompanionCoilBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(COIL_TYPE, CoilType.BOTTOM).setValue(CHARGE, 0).setValue(POWERED, false).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, facingState, level, pos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COIL_TYPE, POWERED, CHARGE, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType computationType) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(COIL_TYPE)) {
			default -> Shapes.block();
			case MIDDLE -> MIDDLE_AABB;
			case TOP -> TOP_AABB;
		};
	}

	public void powerBlock(BlockState state, Level level, BlockPos pos) {
		level.setBlock(pos, state.setValue(POWERED, true).setValue(CHARGE, Math.min(state.getValue(CHARGE) + 1, 10)), 3);
		this.updateNeighbors(state, level, pos);
		level.scheduleTick(pos, this, 20);
	}

	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (state.getValue(COIL_TYPE) == CoilType.BOTTOM && state.getValue(POWERED)) {
			worldIn.setBlock(pos, state.setValue(POWERED, false), 3);
			this.updateNeighbors(state, worldIn, pos);
		}
	}

	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		return state.getValue(COIL_TYPE) == CoilType.BOTTOM && state.getValue(POWERED) ? 15 : 0;
	}

	public int getDirectSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return state.getValue(COIL_TYPE) == CoilType.BOTTOM && state.getValue(POWERED) ? 15 : 0;
	}

	public boolean isSignalSource(BlockState state) {
		return state.getValue(COIL_TYPE) == CoilType.BOTTOM;
	}

	private void updateNeighbors(BlockState state, Level worldIn, BlockPos pos) {
		worldIn.updateNeighborsAt(pos, this);
		worldIn.updateNeighborsAt(pos.below(), this);
	}
}

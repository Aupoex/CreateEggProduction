package com.upo.createeggproduction.content.blocks;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import com.upo.createeggproduction.content.block_entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EggCollectorBlock extends HorizontalKineticBlock implements IBE<EggCollectorBlockEntity>, IRotate {
    public EggCollectorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }
    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }
     @Override
     public SpeedLevel getMinimumRequiredSpeedLevel() {
         return SpeedLevel.SLOW;
     }
    @Override
    public Class<EggCollectorBlockEntity> getBlockEntityClass() {
        return EggCollectorBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends EggCollectorBlockEntity> getBlockEntityType() {
        return ModBlockEntities.EGG_COLLECTOR_BE.get();
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide != null) {
            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide.getOpposite());
        }
        return defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        return Shapes.block();
    }
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }
    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (adjacentBlockState.is(this)) {
            return true;
        }
        return super.skipRendering(state, adjacentBlockState, side);
    }

}






























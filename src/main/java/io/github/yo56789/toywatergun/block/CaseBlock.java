package io.github.yo56789.toywatergun.block;

import com.mojang.serialization.MapCodec;
import io.github.yo56789.toywatergun.item.TWGItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CaseBlock extends HorizontalFacingBlock {
    public static final MapCodec<CaseBlock> CODEC = createCodec(CaseBlock::new);

    public CaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH, SOUTH -> VoxelShapes.cuboid(0.0f, 0.0f, 0.25f, 1.0f, 0.75f, 0.75f);
            case EAST, WEST -> VoxelShapes.cuboid(0.25f, 0.0f, 0.0f, 0.75f, 0.75f, 1.0f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.canModifyBlocks() && player.isSneaking()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), TWGItems.WATER_GUN.getDefaultStack());
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

package io.github.yo56789.toywatergun.block;

import com.mojang.serialization.MapCodec;
import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.item.TWGItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CaseBlock extends BlockWithEntity {
    public static final BooleanProperty OPENED = BooleanProperty.of("opened");

    public CaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(OPENED, false));
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(TWGItems.WATER_GUN) && world.getBlockEntity(pos) instanceof CaseBlockEntity caseBlockEntity) {
            if (caseBlockEntity.containsItem()) {
                // Swap Items????
                return ActionResult.FAIL;
            }

            caseBlockEntity.addItem(stack.copyAndEmpty());

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CaseBlock::new);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPENED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CaseBlockEntity(pos, state);
    }
}

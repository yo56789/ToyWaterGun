package io.github.yo56789.toywatergun.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CaseBlockEntity extends BlockEntity {
    private ItemStack stack;

    public CaseBlockEntity(BlockPos pos, BlockState state) {
        super(TWGBlocks.CASE_BLOCK_ENTITY, pos, state);

        this.stack = ItemStack.EMPTY;
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);

        if (!this.stack.isEmpty()) {
            view.put("item", ItemStack.CODEC, this.stack);
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);

        this.stack = view.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public boolean containsItem() {
        return this.stack != ItemStack.EMPTY;
    }

    public void addItem(ItemStack stack) {
        this.stack = stack;
        markDirty();
    }

    public void removeItem() {
        this.stack = ItemStack.EMPTY;
        markDirty();
    }
}

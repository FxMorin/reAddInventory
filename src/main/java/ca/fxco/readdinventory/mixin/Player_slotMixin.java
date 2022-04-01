package ca.fxco.readdinventory.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.CarriedBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Player.class)
public abstract class Player_slotMixin {

    @Shadow @Final private Inventory inventory;

    @Inject(
            method = "setCarriedBlock(Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At("RETURN")
    )
    public void setCarriedBlock(BlockState blockState, CallbackInfo ci) {
        this.inventory.setItem(0, CarriedBlocks.getItemStackFromBlock(Optional.ofNullable(blockState).orElse(Blocks.AIR.defaultBlockState())));
    }
}

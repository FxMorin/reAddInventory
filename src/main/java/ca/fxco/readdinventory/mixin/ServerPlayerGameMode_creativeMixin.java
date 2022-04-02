package ca.fxco.readdinventory.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameMode_creativeMixin {


    @Redirect(
            method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;" +
                    "Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;" +
                    "Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;" +
                            "setCarriedBlock(Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    public void useItemOn(ServerPlayer instance, BlockState blockState) {
        if (!instance.isCreative()) instance.setCarriedBlock(null);
    }


    @Redirect(
            method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getCarried()" +
                            "Lnet/minecraft/world/entity/LivingEntity$Carried;"
            )
    )
    public LivingEntity.Carried allowBreakingWhileCarrying(ServerPlayer instance) {
        if (instance.isCreative()) return LivingEntity.Carried.NONE;
        return instance.getCarried();
    }


    @Redirect(
            method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setCarriedBlock(" +
                            "Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    public void dontChangeBlockForCreative(ServerPlayer instance, BlockState blockState) {
        if (!instance.isCreative()) instance.setCarriedBlock(blockState);
    }
}

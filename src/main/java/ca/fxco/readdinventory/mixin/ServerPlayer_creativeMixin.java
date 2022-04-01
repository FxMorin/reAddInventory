package ca.fxco.readdinventory.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayer_creativeMixin {


    @Redirect(
            method = "tryThrowBlock(Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;" +
                            "setCarriedBlock(Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    public void infItems(ServerPlayer instance, BlockState blockState) {
        if (!instance.gameMode.isCreative()) instance.setCarriedBlock(null);
    }
}

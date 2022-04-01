package ca.fxco.readdinventory.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class Minecraft_clientMixin {

    @Shadow @Final public Options options;

    @Shadow @Nullable public MultiPlayerGameMode gameMode;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(
            method = "handleKeybinds()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void handleKeybinds(CallbackInfo ci) {
        while (this.options.keyInventory.consumeClick()) {
            if (this.gameMode.isServerControlledInventory()) {
                this.player.sendOpenInventory();
            } else {
                this.setScreen(new InventoryScreen(this.player));
            }
        }
    }

    @ModifyConstant(
            method = "handleKeybinds()V",
            constant = @Constant(intValue = 9)
    )
    private int modifyHotbarSize(int num) {
        return 1;
    }
}

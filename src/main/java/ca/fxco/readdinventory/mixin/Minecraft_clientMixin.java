package ca.fxco.readdinventory.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CarriedBlocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class Minecraft_clientMixin {

    @Shadow
    @Final
    public Options options;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);


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
            this.setScreen(new InventoryScreen(this.player));
        }
    }


    @ModifyConstant(
            method = "handleKeybinds()V",
            constant = @Constant(intValue = 9)
    )
    private int modifyHotbarSize(int num) {
        return 1;
    }


    @Inject(
            method = "pickBlock()V",
            at = @At("RETURN")
    )
    private void onPickBlock(CallbackInfo ci) {
        if (this.player.isCreative()) {
            ItemStack stack = this.player.getInventory().getItem(0);
            this.gameMode.handleCreativeModeItemAdd(stack, 36);
            this.gameMode.handleCreativeModeItemAdd(stack, 0);
            this.player.setCarriedBlock(CarriedBlocks.getBlockFromItemStack(stack).orElse(null));
        }
    }


    @Redirect(
            method = "startAttack()Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;" +
                            "getCarried()Lnet/minecraft/world/entity/LivingEntity$Carried;"
            )
    )
    private LivingEntity.Carried startAttack(LocalPlayer instance) {
        return instance.isCreative() ? LivingEntity.Carried.NONE : instance.getCarried();
    }


    @Redirect(
            method = "continueAttack(Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;" +
                            "getCarried()Lnet/minecraft/world/entity/LivingEntity$Carried;"
            )
    )
    private LivingEntity.Carried onContinueAttack(LocalPlayer instance) {
        return instance.isCreative() ? LivingEntity.Carried.NONE : instance.getCarried();
    }
}

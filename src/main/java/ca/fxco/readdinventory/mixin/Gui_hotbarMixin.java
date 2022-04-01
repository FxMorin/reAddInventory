package ca.fxco.readdinventory.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.components.AbstractWidget.WIDGETS_LOCATION;

@Mixin(Gui.class)
public abstract class Gui_hotbarMixin extends GuiComponent {

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract Player getCameraPlayer();

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Shadow protected abstract void renderSlot(int i, int j, float f, Player player, ItemStack itemStack, int k);

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;" +
                            "getPlayerMode()Lnet/minecraft/world/level/GameType;",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    public void alwaysRender(PoseStack poseStack, float f, CallbackInfo ci) {
        if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            this.renderHotbar(f, poseStack);
        }
    }


    /**
     * @author FX
     * @reason Out of bounds xD
     */
    @Overwrite
    private void renderHotbar(float f, PoseStack poseStack) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            ItemStack itemStack = player.getOffhandItem();
            HumanoidArm humanoidArm = player.getMainArm().getOpposite();
            int i = this.screenWidth / 2;
            int j = this.getBlitOffset();
            this.setBlitOffset(-90);
            this.blit(poseStack, i - 11, this.screenHeight - 22, 0, 0, 22, 22);
            this.blit(poseStack, i - 11 - 1, this.screenHeight - 22 - 1, 0, 22, 24, 22);
            if (!itemStack.isEmpty()) {
                if (humanoidArm == HumanoidArm.LEFT) {
                    this.blit(poseStack, i - 11 - 29, this.screenHeight - 23, 24, 22, 29, 24);
                } else {
                    this.blit(poseStack, i + 11, this.screenHeight - 23, 53, 22, 29, 24);
                }
            }
            this.setBlitOffset(j);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.renderSlot(i - 8, this.screenHeight - 16 - 3, f, player, player.getInventory().items.get(0), 1);
            if (!itemStack.isEmpty()) {
                int n = this.screenHeight - 16 - 3;
                if (humanoidArm == HumanoidArm.LEFT) {
                    this.renderSlot(i - 11 - 26, n, f, player, itemStack, 2);
                } else {
                    this.renderSlot(i + 11 + 10, n, f, player, itemStack, 2);
                }
            }
            RenderSystem.disableBlend();
        }
    }


    @ModifyArg(
            method = "renderHearts(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Gui$HeartType;IIIZZ)V"
            ),
            index = 3
    )
    private int renderHeartHeight(int i) {
        return i-22;
    }
}

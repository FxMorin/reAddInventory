package ca.fxco.readdinventory.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CarriedBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public class LocalPlayer_dontDropMixin extends AbstractClientPlayer {

    @Shadow @Final public ClientPacketListener connection;

    public LocalPlayer_dontDropMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    /**
     * @author FX
     * @reason Stop dropping items which just results in throwing blocks
     */
    @Overwrite
    public boolean drop(boolean bl) {
        if (this.isCreative()) {
            this.getInventory().setItem(0,ItemStack.EMPTY);
            this.setCarriedBlock(null);
            ItemStack stack = this.getInventory().getItem(0);
            this.connection.send((new ServerboundSetCreativeModeSlotPacket(36, stack)));
            this.connection.send((new ServerboundSetCreativeModeSlotPacket(0, stack)));
        }
        return false;
    }
}

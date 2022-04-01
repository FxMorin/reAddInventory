package ca.fxco.readdinventory.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CarriedBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Inventory.class)
public abstract class Inventory_1Mixin {

    @Shadow
    private int timesChanged;

    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    public NonNullList<ItemStack> items;

    @Shadow
    public abstract ItemStack getItem(int i);

    @Shadow
    public abstract void setItem(int i, ItemStack itemStack);


    @ModifyConstant(
            method = "<init>(Lnet/minecraft/world/entity/player/Player;)V",
            constant = @Constant(intValue = 36)
    )
    private int modifyInventorySize(int num) {
        return 1;
    }


    /**
     * @author FX
     * @reason its 1 now
     */
    @Overwrite
    public static int getSelectionSize() {
        return 1;
    }


    /**
     * @author FX
     * @reason its 1 now
     */
    @Overwrite
    public static boolean isHotbarSlot(int i) {
        return i == 0;
    }


    /**
     * @author FX
     * @reason its 1 now
     */
    @Overwrite
    public int getSuitableHotbarSlot() {
        return 0;
    }


    /**
     * @author FX
     * @reason its 1 now
     */
    @Overwrite
    public void swapPaint(double d) {}


    /**
     * @author FX
     * @reason Set carried block
     */
    @Overwrite
    public void setChanged() {
        ++this.timesChanged;
        ItemStack stack = this.getItem(0);
        this.items.clear();
        this.setItem(0,stack);
        this.player.setCarriedBlock(CarriedBlocks.getBlockFromItemStack(this.getItem(0)).orElse(null));
    }
}

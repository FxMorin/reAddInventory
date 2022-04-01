package ca.fxco.readdinventory.mixin;

import ca.fxco.readdinventory.stuff.FakeSlot;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenu_smallMixin extends RecipeBookMenu<CraftingContainer> {

    public InventoryMenu_smallMixin(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Redirect(
           method = "<init>",
           slice = @Slice(
               from = @At("HEAD"),
               to = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/world/inventory/InventoryMenu;" +
                               "addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
                       shift = At.Shift.BEFORE,
                       ordinal = 2
               )
           ),
           at = @At(
                   value = "NEW",
                   target = "net/minecraft/world/inventory/Slot"
           )
    )
    private Slot dontMakeTheseSlots1(Container container, int i, int j, int k) {
        return new FakeSlot(container,i,j,k);
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/world/inventory/Slot",
                    ordinal = 1
            )
    )
    private Slot dontMakeTheseSlots2(Container container, int i, int j, int k) {
        return new FakeSlot(container,i,j,k);
    }

    @Redirect(
            method = "<init>",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/inventory/InventoryMenu;" +
                                    "addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
                            shift = At.Shift.AFTER,
                            ordinal = 3
                    )
            ),
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/world/inventory/Slot"
            )
    )
    private Slot changeSoOnlyOneHappens(Container container, int i, int j, int k) {
        return i == 0 ? new Slot(container,i,j,k) : new FakeSlot(container,i,j,k);
    }
}

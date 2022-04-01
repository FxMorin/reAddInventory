package ca.fxco.readdinventory.stuff;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FakeSlot extends Slot {
    public FakeSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    public boolean mayPickup(Player player) {
        return false;
    }

    public boolean isActive() {
        return false;
    }
}

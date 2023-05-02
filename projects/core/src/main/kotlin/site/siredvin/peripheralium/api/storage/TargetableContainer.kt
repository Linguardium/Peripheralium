package site.siredvin.peripheralium.api.storage

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

class TargetableContainer(private val container: Container): SlottedStorage {
    override fun takeItems(limit: Int, startSlot: Int, endSlot: Int, predicate: Predicate<ItemStack>): ItemStack {
        return ContainerUtils.takeItems(container, limit, startSlot, endSlot, predicate)
    }

    override fun getItem(slot: Int): ItemStack {
        return container.getItem(slot)
    }

    override fun canPlaceItem(slot: Int, item: ItemStack): Boolean {
        return container.canPlaceItem(slot, item)
    }

    override fun storeItem(stack: ItemStack, startSlot: Int, endSlot: Int): ItemStack {
        return ContainerUtils.storeItem(container, stack, startSlot, endSlot)
    }

    override fun setChanged() {
        container.setChanged()
    }

    override val size: Int
        get() = container.containerSize

}
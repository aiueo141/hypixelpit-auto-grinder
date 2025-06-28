package org.polyfrost.example.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemAxe;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.util.function.Predicate;

public class AutoHeal {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static boolean swapSlot(Predicate<ItemStack> condition) {
        for (int slot = 8; slot >= 0; slot--) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
            if (itemStack != null && condition.test(itemStack)) {
                mc.thePlayer.inventory.currentItem = slot;
                return true;
            }
        }

        return false;
    }

    private static boolean shouldHeal() {
        return mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth();
    }

    private static boolean isSword(ItemStack is) {
        return is != null && is.getItem() instanceof ItemSword || is.getItem() instanceof ItemAxe;
    }

    private static boolean isHealItem(ItemStack is) {
        if (is == null) return false;

        Item item = is.getItem();
        return item == Items.skull || item == Items.baked_potato;
    }

    public static void healIfNeeded() {
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (shouldHeal()) {
            if (isHealItem(heldItem)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(heldItem));
            } else if (!swapSlot(AutoHeal::isHealItem)) {
                swapSlot(AutoHeal::isSword);
            }
        } else {
            swapSlot(AutoHeal::isSword);
        }
    }
}
package org.polyfrost.example.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;

public class InventoryManager {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static boolean moving = false;
    private static int slotToMove = 1;
    private static int delayTicks = 0;
    private static final int delayBetweenClicks = 5; // 5tick ≒ 250ms


    public static boolean isHotbarFull() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) return false;
        }
        return true;
    }

    public static void openInventoryIfNeeded() {
        if (!(mc.currentScreen instanceof GuiInventory)) {
            mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
        }
    }


    public static void resetState() {
        moving = false;
        slotToMove = 1;
        delayTicks = 0;
    }


    public static void startSwapToInventory() {
        if (isInventoryFull()) {

            return;
        }
        if (isHotbarFull()) {
            openInventoryIfNeeded();
            moving = true;
            slotToMove = 1;
            delayTicks = 0;
        }
    }

    public static void inventoryManage() {
        if (!moving) return;


        if (!(mc.currentScreen instanceof GuiInventory)) {
            resetState();
            return;
        }

        mc.thePlayer.setJumping(false);

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);

        if (delayTicks > 0) {
            delayTicks--;
            return;
        }

        if (slotToMove >= 9) {
            resetState();
            mc.thePlayer.closeScreen();
            return;
        }

        ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slotToMove);
        if (stack != null) {
            int guiSlot = 36 + slotToMove;
            mc.playerController.windowClick(0, guiSlot, 0, 1, mc.thePlayer);
        }

        slotToMove++;
        delayTicks = delayBetweenClicks;
    }

    public static boolean isMoving() {
        return moving;
    }

    public static boolean isInventoryFull() {
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) return false;
        }
        return true;
    }

}

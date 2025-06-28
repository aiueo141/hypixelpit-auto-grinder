package org.polyfrost.example.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class ResetAllMovements {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static void immaResetAl() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }
}

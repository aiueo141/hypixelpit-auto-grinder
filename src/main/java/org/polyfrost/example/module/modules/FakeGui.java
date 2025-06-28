package org.polyfrost.example.module.modules;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.polyfrost.example.AutoGrinderMod;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.module.Module;

public class FakeGui extends GuiScreen {

    @Override
    public void keyTyped(char t, int k) {
        if (k == AutoGrinderConfig.keyBind.getKeyBinds().get(0)) {
            this.mc.displayGuiScreen(null);
            Module module = AutoGrinderMod.moduleManager.toggleSprintModule;
            module.setEnabled(false);
        }
    }
}

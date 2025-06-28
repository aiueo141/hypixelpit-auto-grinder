package org.polyfrost.example.module;

public class Module {
    private boolean isEnabled;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        if (!enabled) {
            onDisable();
        }
        if (enabled) {
            onEnable();
        }
    }

    public void onDisable() {

    }

    public void onEnable() {

    }
}

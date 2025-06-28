package org.polyfrost.example.module;

import org.polyfrost.example.module.modules.AutoGrindModule;

public class ModuleManager {
    public AutoGrindModule toggleSprintModule;
    public ModuleManager() {
        toggleSprintModule = new AutoGrindModule();
    }
}

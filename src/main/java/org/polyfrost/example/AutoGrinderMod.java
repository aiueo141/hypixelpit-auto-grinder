package org.polyfrost.example;

import cc.polyfrost.oneconfig.hud.Hud;
import org.polyfrost.example.config.AutoGrinderConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.example.module.ModuleManager;

@Mod(modid = AutoGrinderMod.MODID, name = AutoGrinderMod.NAME, version = AutoGrinderMod.VERSION)
public class AutoGrinderMod {

    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    @Mod.Instance(MODID)
    public static AutoGrinderMod INSTANCE;

    public static AutoGrinderConfig config;

    public static ModuleManager moduleManager;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new AutoGrinderConfig();
        moduleManager = new ModuleManager();
    }
}

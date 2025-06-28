package org.polyfrost.example.config;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import org.polyfrost.example.AutoGrinderMod;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.polyfrost.example.module.Module;

import javax.swing.text.html.Option;

public class AutoGrinderConfig extends Config {

    @KeyBind(
            name = "Key"
    )
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_LSHIFT, UKeyboard.KEY_S);

    @Slider(
            name = "Jump chance",
            min = 0, max = 100
    )
    public static int jumpChance = 50;

    @Slider(
            name = "Require minimum mid players",
            min = 0, max = 50
    )
    public static int requireMinimumMidPlayers = 20;

    @Slider(
            name = "Horizontal aim speed",
            min = 1f, max = 9f
    )
    public static float horizontalAimSpeed = 2.9f;

    @Slider(
            name = "Vertical aim speed",
            min = 1f, max = 9f
    )
    public static float verticalAimSpeed = 2f;

    @Slider(
            name = "Spawn cooldown",
            min = 0f, max = 25f
    )
    public static float spawnCooldown = 1f;

    @Slider(
            name = "Stuck detection time",
            min = 1, max = 10
    )
    public static float stuckDetectionTime = 5f;

    @Text(
            name = "Target lobby ",
            placeholder = " ",        // optional, text to display when there is nothing written there
            secure = false, multiline = false
    )
    public static String targetLobby = "";

    @Switch(
            name = "Ignore Diamond Wearers (Do not use this for silent botting)",
            size = OptionSize.SINGLE // optional
    )
    public static boolean excludeDiamonds = false;

    @Switch(
            name = "Myau KillAura",
            size = OptionSize.SINGLE // optional
    )
    public static boolean toggleAura = false;

    @Switch(
            name = "Myau AutoHeal",
            size = OptionSize.SINGLE
    )
    public static boolean toggleAutoheal = false;

    @Switch(
            name = "AutoPerk (unimplemented)",
            size = OptionSize.SINGLE
    )
    public static boolean isAutoPerk = false;

    public AutoGrinderConfig() {
        super(new Mod(AutoGrinderMod.NAME, ModType.UTIL_QOL), AutoGrinderMod.MODID + ".json");
        initialize();
        registerKeyBind(keyBind, () -> {
            Module module = AutoGrinderMod.moduleManager.toggleSprintModule;
            module.setEnabled(!module.isEnabled());

        });
    }
}

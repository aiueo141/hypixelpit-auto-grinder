package org.polyfrost.example.module.modules;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import org.polyfrost.example.config.AutoGrinderConfig;

public class PitMap {

    public static int MID_Y_COORDINATE;
    public static int SPAWN_Y_COORDINATE;

    public static void updateMapType(PitMapType type) {
        if (type == null) return;
        MID_Y_COORDINATE = type.getFloorY();
        SPAWN_Y_COORDINATE = type.getSpawnY();
        UChat.chat("Map type updated to: " + type.getName());
    }
}

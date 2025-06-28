package org.polyfrost.example.module.modules;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.utils.AimUtils;
import org.polyfrost.example.utils.Timer;

import java.util.Random;

public class SpawnMoveAction {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Random rand = new Random();
    private static final int DEFAULT_AIM_Y = PitMap.SPAWN_Y_COORDINATE -8;
    private static final Timer justTimer = new Timer();

    public static int midDropAimingY, midDropAimingX, midDropAimingZ;
    public static int spawnJumpChance;

    public static void initialize() {
        spawnJumpChance = 1;//rand.nextInt(1);
        midDropAimingY = rand.nextInt(6);
        midDropAimingX = rand.nextInt(5) - 2;
        midDropAimingZ = rand.nextInt(5) - 2;
        midDropAimingY = midDropAimingY + DEFAULT_AIM_Y;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        justTimer.reset();
    }

    public static void moveFromSpawn() {
        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;
// 3.5 なのはkingsでたまにstuckすることがあるから
        AimUtils.aimTo(new BlockPos(midDropAimingX, midDropAimingY, midDropAimingZ), new double[]{6.3, 9});
        //UChat.chat("Aiming to" + " " + midDropAimingX + " " + midDropAimingY + " " + midDropAimingZ);
        switch (spawnJumpChance) {
            case 0:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                mc.thePlayer.setSprinting(true);
                break;
            case 1:
            case 2:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                mc.thePlayer.setSprinting(true);
                if (justTimer.passed(AutoGrinderConfig.spawnCooldown * 1000 + 250)) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                    justTimer.reset();
                }
                break;
            case 3:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                if (Math.sqrt(x * x + z * z) <= 7)
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                mc.thePlayer.setSprinting(true);
                break;
        }
    }
}

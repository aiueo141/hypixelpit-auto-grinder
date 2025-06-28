package org.polyfrost.example.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.MathHelper;
import org.polyfrost.example.utils.AimUtils;
import org.polyfrost.example.config.AutoGrinderConfig;

import java.util.Random;

import static org.polyfrost.example.utils.AimUtils.isVisible;

public class MidMoveAction {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Random rand = new Random();
    private static int CollidedTimer = 0;
    private static EntityPlayer cachedTarget = null;
    private static int targetCheckTick = 0;

    public static void processFollowingMove() {
        // 5tickに1回だけターゲット更新（約0.25秒に1回）
        if (targetCheckTick++ >= 20
                || cachedTarget == null
                || cachedTarget.isDead
                || cachedTarget.isInvisible()
                || !mc.theWorld.playerEntities.contains(cachedTarget)
                || !isVisible(mc.thePlayer, cachedTarget)) {
            cachedTarget = AimUtils.getTarget(13, 3, mc.thePlayer, 0, PitMap.MID_Y_COORDINATE, 0);
            targetCheckTick = 0;
        }

        EntityPlayer target = cachedTarget;

        if (target == null) {
            CollidedTimer++;
            if (CollidedTimer >= 40) {
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/oof"));
                CollidedTimer = 0;
            }
            return;
        } else {
            CollidedTimer = 0;
        }

        if (mc.thePlayer.isCollidedHorizontally) {
            CollidedTimer++;
            if (CollidedTimer >= AutoGrinderConfig.stuckDetectionTime * 20) {
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/oof"));
                CollidedTimer = 0;
            }
        } else {
            CollidedTimer = 0;
        }

        AimUtils.aimTo(target, new double[]{10 - AutoGrinderConfig.horizontalAimSpeed, 10 - AutoGrinderConfig.verticalAimSpeed});

        float targetYaw = AimUtils.getTargetRotations(target.posX, target.posY, target.posZ)[0];
        float yawDifference = MathHelper.wrapAngleTo180_float(targetYaw - mc.thePlayer.rotationYaw);

        double dz = mc.thePlayer.posZ - target.posZ;
        double dx = mc.thePlayer.posX - target.posX;

        double dist = (dx * dx + dz * dz);

        // キー操作リセット
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true); // 常に前進

        if (yawDifference < -15) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
        } else if (yawDifference > 15) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
        }

        if (rand.nextInt(100) < AutoGrinderConfig.jumpChance) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        }
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);

        if (dist <= 1) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        }
    }
}

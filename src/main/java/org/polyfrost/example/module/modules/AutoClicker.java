package org.polyfrost.example.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.polyfrost.example.utils.AimUtils;

public class AutoClicker {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void click() {
        Entity entityHit = mc.objectMouseOver.entityHit;
        if (Math.random() < 0.5) {
            if (entityHit != null) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, entityHit);
            } else {
                EntityPlayer target = AimUtils.getTarget(13, 3, mc.thePlayer, 0, PitMap.MID_Y_COORDINATE, 0);
                if (target == null) return;
                if (mc.thePlayer.getDistanceToEntity(target) < 5) {
                    mc.thePlayer.swingItem();
                }
            }
        }
    }

    public static void press() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
    }
}

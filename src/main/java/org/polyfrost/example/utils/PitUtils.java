package org.polyfrost.example.utils;

import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.module.modules.PitMap;

public class PitUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int getMidPlayerCount() {
        int targetCount = 0;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != null) {
                double x = player.posX;
                double y = player.posY;
                double z = player.posZ;
                double distFromOrigin = Math.sqrt(x * x + z * z);
                // プレイヤーがmid内にいてダイヤ装備除外がオンならダイヤ装備を除外する
                if (distFromOrigin <= 14 && y <= PitMap.MID_Y_COORDINATE + 5 && (!AutoGrinderConfig.excludeDiamonds || !AimUtils.hasDiamondChestplate(player))) {
                    targetCount = targetCount + 1;
                }
            }
        }
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Mid Players" + " " + targetCount));
        return targetCount;
    }
}

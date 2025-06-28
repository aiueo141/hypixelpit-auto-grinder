package org.polyfrost.example.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.module.modules.PitMap;

import static org.polyfrost.example.utils.AimUtils.aimTo;

public class MysticFinder {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean mysticFinder() {
        Minecraft mc = Minecraft.getMinecraft();
        double maxRange = 14.0;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityItem)) continue;

            EntityItem entityItem = (EntityItem) entity;
            ItemStack itemStack = entityItem.getEntityItem();

            String displayName = itemStack.getDisplayName();
            if (!displayName.contains("Fresh") && !displayName.contains("Mystic")) continue;

            double dx = entityItem.posX - 0.0;
            double dz = entityItem.posZ - 0.0;
            double distanceXZ = Math.sqrt(dx * dx + dz * dz);

            if (distanceXZ <= maxRange) {

                double[] smoothing = new double[]{5, 5};
                AimUtils.aimTo(entityItem, smoothing);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                return true;
            }
        }
        return false;
    }
}

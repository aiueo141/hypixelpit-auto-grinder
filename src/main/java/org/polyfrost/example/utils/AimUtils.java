package org.polyfrost.example.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.module.modules.PitMap;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AimUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();


    public static EntityPlayer getTarget(double range, double attackrange, EntityPlayer skipentity, double centerX, double centerY, double centerZ) {
        double rangeSq = range * range;
        List<EntityPlayer> targets = mc.theWorld.playerEntities;
        targets = targets.stream()
                .filter(entity -> entity.getPosition().distanceSq(centerX, centerY, centerZ) <= rangeSq
                        && entity != skipentity
                        && entity.posY < mc.thePlayer.posY + 3
                        && entity.posY > mc.thePlayer.posY - 14
                        && entity != mc.thePlayer
                        && !entity.isDead
                        && !entity.isInvisible()
                        && (!AutoGrinderConfig.excludeDiamonds || !AimUtils.hasDiamondChestplate(entity))
                        && isVisible(mc.thePlayer, entity)
                        && entity.posY <= PitMap.SPAWN_Y_COORDINATE
                        && entity.posY < PitMap.MID_Y_COORDINATE + 5)
                .collect(Collectors.toList());

        targets.sort(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity)));

        List<EntityPlayer> attacktargets = targets.stream()
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) <= attackrange + 2)
                .collect(Collectors.toList());

        attacktargets.sort(Comparator
                .comparingDouble(EntityPlayer::getHealth)
                .thenComparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity))
                .thenComparingDouble(entity ->
                        AimUtils.angledifference(
                                AimUtils.getTargetRotations(entity.posX, entity.posY, entity.posZ)[0],
                                mc.thePlayer.rotationYaw)));

        if (!attacktargets.isEmpty()) {
            return attacktargets.get(0);
        } else if (!targets.isEmpty()) {
            return targets.get(0);
        }

        return null;
    }

    public static boolean hasDiamondChestplate(EntityPlayer player) {
        for (ItemStack itemStack : player.inventory.armorInventory) {
            if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) itemStack.getItem();
                if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.DIAMOND && armor.armorType == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void aimTo(BlockPos positionAt, double[] i) {
        aimTo(positionAt.getX() + 0.5, positionAt.getY() + 0.5, positionAt.getZ() + 0.5, i);
    }

    public static void aimTo(Entity entity, double[] i) {
        double y;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase en = (EntityLivingBase) entity;
            y = en.posY + (double) en.getEyeHeight() * 0.9D;
        } else {
            y = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D;
        }
        aimTo(entity.posX + 0.5, y, entity.posZ + 0.5, i);
    }

    public static void aimTo(double x, double y, double z, double[] i) {
        float[] t = smoothRotations(getTargetRotations(x, y, z), i[0] / 10, i[1] / 10);
        final float[] rotations = new float[]{t[0], t[1]};
        final float[] lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        final float[] fixedRotations = getFixedRotation(rotations, lastRotations);

        mc.thePlayer.rotationYaw = fixedRotations[0];
        mc.thePlayer.rotationPitch = clamp(fixedRotations[1], -80.0F, 80.0F); // ← ここで安全に制限！
    }


    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }



    public static float[] getTargetRotations(double x, double y, double z) {
        double diffX = x - (mc.thePlayer.posX + 0.5);
        double diffY = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double diffZ = z - (mc.thePlayer.posZ + 0.5);
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{yaw, pitch + 11};
    }

    public static float[] smoothRotations(float[] rotations, double smooth, double smooth2) {
        float angleDifference = angledifference(rotations[0], mc.thePlayer.rotationYaw);
        float angleDifference2 = angledifference(rotations[1], mc.thePlayer.rotationPitch);

        double YawCalculation = (1 - smooth) * (1.0 + 0.1 * Math.abs(angleDifference) / 180.0);
        double PitchCalculation = (1 - smooth2) * (1.0 + 0.1 * Math.abs(angleDifference2) / 180.0);

        return new float[]{(float) (mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(rotations[0] - mc.thePlayer.rotationYaw) * YawCalculation), (float) (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(rotations[1] - mc.thePlayer.rotationPitch) * PitchCalculation)};
    }

    public static float angledifference(float rotation1, float rotation2) {
        return MathHelper.wrapAngleTo180_float(rotation1 - rotation2);
    }

    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }

    public static boolean isVisible(EntityPlayer from, EntityPlayer to) {
        Vec3 fromVec = new Vec3(from.posX, from.posY + from.getEyeHeight(), from.posZ);
        Vec3 toVec = new Vec3(to.posX, to.posY + to.getEyeHeight(), to.posZ);
        MovingObjectPosition result = from.worldObj.rayTraceBlocks(
                fromVec,
                toVec,
                false,
                true,
                false
        );

        return result == null || result.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }


}

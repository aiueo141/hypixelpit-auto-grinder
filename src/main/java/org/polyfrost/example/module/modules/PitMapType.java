package org.polyfrost.example.module.modules;

import lombok.Getter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum PitMapType {
    ABYSS("Abyss", 114, 20, 82, 9.9),
    GENESIS("Genesis", 86, 30, 43, 10.9),
    SEASONS("Seasons", 114, 20, 82, 9.9),
    ELEMENTS("Elements", 114, 20, 82, 9.9),
    CASTLE("Castle", 95, 20, 71, 9.9);

    private final String name;
    private final int spawnY;
    private final int spawnRadius;
    private final int floorY;
    private final double floorRadius;
    private final Set<BlockPos> circle;

    PitMapType(String name, int spawnY, int spawnRadius, int floorY, double floorRadius ) {
        this.name = name;
        this.spawnY = spawnY;
        this.spawnRadius = spawnRadius;
        this.floorY = floorY;
        this.floorRadius = floorRadius;
        this.circle = getCircle(floorY, floorRadius);
    }

    public static PitMapType fromName(String name) {
        for (PitMapType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    private static Set<BlockPos> getCircle(int floorY, double radius) {
        Set<BlockPos> blocks = new HashSet<>();
        for (int x = -MathHelper.ceiling_double_int(radius + 1); x <= MathHelper.ceiling_double_int(radius + 1); x++) {
            for (int z = -MathHelper.ceiling_double_int(radius + 1); z <= MathHelper.ceiling_double_int(radius + 1); z++) {
                double cx = x - 0.5;
                double cz = z - 0.5;
                if (cx * cx + cz * cz <= radius * radius) {
                    blocks.add(new BlockPos(x, floorY, z));
                }
            }
        }

        Set<BlockPos> filtered = new HashSet<>();
        for (BlockPos pos : blocks) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                BlockPos offset = pos.offset(facing);
                if (!blocks.contains(offset)) {
                    filtered.add(pos);
                    break;
                }
            }
        }

        return filtered;
    }
}
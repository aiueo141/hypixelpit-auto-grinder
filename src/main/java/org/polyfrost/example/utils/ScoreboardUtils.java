package org.polyfrost.example.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardUtils {

    public static List<String> getScoreboardLines() {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (world == null) return Collections.emptyList();

        Scoreboard scoreboard = world.getScoreboard();
        if (scoreboard == null) return Collections.emptyList();

        ScoreObjective obj = scoreboard.getObjectiveInDisplaySlot(1);
        if (obj == null) return Collections.emptyList();

        List<String> lines = scoreboard.getSortedScores(obj)
                .stream()
                .map(it -> ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(it.getPlayerName()), it.getPlayerName()))
                .map(it -> it.replaceAll("[^\\x00-\\x7F|§]", "").replaceAll("§[0123456789abcdefklmnor]", ""))
                .collect(Collectors.toList());

        Collections.reverse(lines);
        return lines.subList(0, Math.min(lines.size(), 15));
    }

    public static String getScoreboardDisplayName() {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (world == null) return "";

        Scoreboard scoreboard = world.getScoreboard();
        if (scoreboard == null) return "";

        ScoreObjective obj = scoreboard.getObjectiveInDisplaySlot(1);
        if (obj == null) return "";
        return obj.getDisplayName().replaceAll("[^\\x00-\\x7F|§]", "").replaceAll("§[0123456789abcdefklmnor]", "");
    }
}

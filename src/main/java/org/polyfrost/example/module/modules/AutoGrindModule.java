package org.polyfrost.example.module.modules;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.polyfrost.example.config.AutoGrinderConfig;
import org.polyfrost.example.module.Module;
import org.polyfrost.example.utils.*;

import java.util.List;

public class AutoGrindModule extends Module {

    private enum PlayAction {
        SPAWN_MOVE,
        MID_MOVE,
    }

    private static final Minecraft mc = Minecraft.getMinecraft();

    private final Timer lobbySwappingTimer = new Timer();
    private final Timer spawnWaitTimer = new Timer();
    private final Timer locrawTimer = new Timer();
    private final Timer playPitCooldownTimer = new Timer();


    private PlayAction prevAction;
    private double prevPosY = 999;
    private double invPosY;
    private int tempCount = 0;
    private int locrawhandler = 0;

    private boolean justJoinedHypixel = false;
    private boolean justEnteredPit = false;
    private boolean coordinateviewer = false;
    private boolean allowShouldGoToLobby = false;
    private boolean allowAutoGrind = false;

    @Override
    public void onEnable() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("AutoGrinder: §bON"));
        locrawTimer.reset();
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("AutoGrinder: §cOFF"));
        SpawnMoveAction.initialize();

    }

    public AutoGrindModule() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MapDetector());
    }

    private boolean shouldGoToLobby() {

        double limboY = mc.thePlayer.getPosition().getY();

        if (limboY == 31)
            return true;
        if (allowShouldGoToLobby) {


        if (ScoreboardUtils.getScoreboardLines().size() < 3) return false;
        if (mc.theWorld.getBlockState(new BlockPos(0, mc.thePlayer.getPosition().getY() - 1, 0)).getBlock() != Blocks.air
                || ScoreboardUtils.getScoreboardLines().get(2).contains("PIZZA")
                || ScoreboardUtils.getScoreboardLines().get(2).contains("SPIRE")
                || ScoreboardUtils.getScoreboardLines().get(2).contains("SQUADS")
                || ScoreboardUtils.getScoreboardLines().get(2).contains("RAFFLE")) {
            ResetAllMovements.immaResetAl();
            return false;
        }


        if (!ScoreboardUtils.getScoreboardDisplayName().contains("PIT")) return false;

        if (((!ScoreboardUtils.getScoreboardLines().get(0).contains("RAGE") &&
                !ScoreboardUtils.getScoreboardLines().get(1).contains("RAGE") &&
                !ScoreboardUtils.getScoreboardLines().get(2).contains("RAGE") && PitUtils.getMidPlayerCount() < AutoGrinderConfig.requireMinimumMidPlayers)))
            return true;

        if (AutoGrinderConfig.targetLobby.isEmpty()) return false;

        List<String> lines = ScoreboardUtils.getScoreboardLines();
        if (lines.isEmpty() || lines.get(0).contains(AutoGrinderConfig.targetLobby)) return false;

        return true;
    }
        return false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !isEnabled() || mc.thePlayer == null) return;


        if (InventoryManager.isMoving()) {
            InventoryManager.inventoryManage();
            return;
        }
        invPosY = mc.thePlayer.getPosition().getY();

        if (InventoryManager.isHotbarFull() && !InventoryManager.isMoving() && invPosY >= PitMap.SPAWN_Y_COORDINATE) {
            InventoryManager.startSwapToInventory();
        }

        if (shouldGoToLobby() && lobbySwappingTimer.passed(10_000)) {
            UChat.say("/lobby");
            UChat.chat("JOINING THE LOBBY");

            if (AutoGrinderConfig.toggleAura) {
                UChat.say(".c load ag");
                UChat.chat("RESET MYAU CONFIG");
                prevPosY = 999;
                tempCount = 0;
            }

            lobbySwappingTimer.reset();
        }


        if (!ScoreboardUtils.getScoreboardDisplayName().contains("PIT")) {
            justEnteredPit = false;

            if (AutoGrinderConfig.toggleAura && tempCount == 0) {
                UChat.say(".c load ag");
                UChat.chat("RESET MYAU CONFIG");
                UChat.chat("RESET PREV POSY");
                prevPosY = 999;
                tempCount = 1;
            }

            ResetAllMovements.immaResetAl();
        }

        boolean inHypixel = ScoreboardUtils.getScoreboardDisplayName().equals("HYPIXEL");
        if (inHypixel && !justJoinedHypixel && playPitCooldownTimer.passed(10_000)) {
            justJoinedHypixel = true;
            allowShouldGoToLobby = false;

            if (AutoGrinderConfig.toggleAura) {
                UChat.say(".c load ag");
                UChat.chat("RESET MYAU CONFIG");
                UChat.chat("RESET PREV POSY");
                prevPosY = 999;
                tempCount = 0;
            }
            UChat.say("/play pit");
            UChat.chat("JOINING THE PIT");

            playPitCooldownTimer.reset();
            spawnWaitTimer.reset();
            locrawTimer.reset();
            locrawhandler = 0;
            allowAutoGrind = false;
        }

        if (!inHypixel) {
            justJoinedHypixel = false;
            playPitCooldownTimer.reset();
        }
        if (shouldGoToLobby() || !ScoreboardUtils.getScoreboardDisplayName().contains("PIT")) return;

        if (!justEnteredPit && locrawTimer.passed(2_000)) {
            UChat.say("/locraw");
            UChat.chat("sent locraw");
            locrawhandler = 1;
            justEnteredPit = true;
            coordinateviewer = true;
            allowShouldGoToLobby = true;
            allowAutoGrind = true;
            lobbySwappingTimer.reset();
        }



        if (PitMap.SPAWN_Y_COORDINATE == 0 || PitMap.MID_Y_COORDINATE == 0) {
            UChat.chat("Waiting Map Detection...");
            return;
    }
        if (coordinateviewer) {
            UChat.chat("Using coordinates: " + PitMap.MID_Y_COORDINATE + ", " + PitMap.SPAWN_Y_COORDINATE);
            coordinateviewer = false;
        }

        double KILLAURA_TOGGLE_Y = PitMap.SPAWN_Y_COORDINATE - (PitMap.SPAWN_Y_COORDINATE - PitMap.MID_Y_COORDINATE) / 2;

        if (AutoGrinderConfig.toggleAura) {
            double posY = mc.thePlayer.posY;
            if ((posY - KILLAURA_TOGGLE_Y) * (this.prevPosY - KILLAURA_TOGGLE_Y) < 0) {
                UChat.say(".t killaura");
            }
            this.prevPosY = posY;
        }



        if (allowAutoGrind && !shouldGoToLobby()) {

            PlayAction action = PitMap.SPAWN_Y_COORDINATE - 1 <= mc.thePlayer.getPosition().getY() ? PlayAction.SPAWN_MOVE : PlayAction.MID_MOVE;
            PlayAction prevAction = this.prevAction;
            this.prevAction = action;


            switch (action) {
                case SPAWN_MOVE:
                    if (action != prevAction) {
                        SpawnMoveAction.initialize();
                        spawnWaitTimer.reset();
                    }
                    if (ScoreboardUtils.getScoreboardLines().size() < 3) return;
                    if (mc.theWorld.getBlockState(new BlockPos(0, mc.thePlayer.getPosition().getY() - 1, 0)).getBlock() != Blocks.air
                            || ScoreboardUtils.getScoreboardLines().get(2).contains("PIZZA")
                            || ScoreboardUtils.getScoreboardLines().get(2).contains("SPIRE")
                            || ScoreboardUtils.getScoreboardLines().get(2).contains("SQUADS")
                            || ScoreboardUtils.getScoreboardLines().get(2).contains("RAFFLE"))
                        return;
                    if (spawnWaitTimer.passed(AutoGrinderConfig.spawnCooldown * 1000)) {
                        SpawnMoveAction.moveFromSpawn();
                    }
                    break;
                case MID_MOVE:
                    if (!MysticFinder.mysticFinder()) {
                        MidMoveAction.processFollowingMove();
                        if (!AutoGrinderConfig.toggleAutoheal) {
                            AutoHeal.healIfNeeded();
                        }
                    }
                    if (!AutoGrinderConfig.toggleAura) {
                        AutoClicker.click();
                    }
                    break;
            }
        }


    }
}

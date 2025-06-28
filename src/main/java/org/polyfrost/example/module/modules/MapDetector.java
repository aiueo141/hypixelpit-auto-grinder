package org.polyfrost.example.module.modules;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.polyfrost.example.module.modules.PitMap;
import org.polyfrost.example.module.modules.PitMapType;

public class MapDetector {

    public static PitMapType currentMapType;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

        try {
            JsonObject obj = new Gson().fromJson(message, JsonObject.class);
            if (obj != null && obj.has("map")) {
                String mapName = obj.get("map").getAsString();
                currentMapType = PitMapType.fromName(mapName);
                PitMap.updateMapType(currentMapType);
            }
        } catch (JsonSyntaxException ignored) {
        }
    }
}
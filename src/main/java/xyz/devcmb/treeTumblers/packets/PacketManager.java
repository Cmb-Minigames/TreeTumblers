package xyz.devcmb.treeTumblers.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import xyz.devcmb.treeTumblers.packets.listeners.ScoreNumbers;

public class PacketManager {
    public static void registerAllPacketListeners() {
        registerPacketListener(new ScoreNumbers(), PacketListenerPriority.LOW);
    }

    private static void registerPacketListener(PacketListener listener, PacketListenerPriority priority) {
        EventManager eventManager = PacketEvents.getAPI().getEventManager();
        eventManager.registerListener(listener, priority);
    }
}
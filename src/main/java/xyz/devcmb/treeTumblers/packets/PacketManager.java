package xyz.devcmb.treeTumblers.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {
    public static List<PacketManipulator> packetManipulators = new ArrayList<>();
    public static void RegisterPacketManipulator(PacketManipulator packetManipulator) {
        if(packetManipulator.IsDefault()){
            PacketEvents.getAPI().getEventManager().registerListener(packetManipulator, PacketListenerPriority.HIGH);
        }

        packetManipulators.add(packetManipulator);
    }

    public static void RegisterPacketManipulators(){
        RegisterPacketManipulator(new HideScoreNumbers());
    }
}

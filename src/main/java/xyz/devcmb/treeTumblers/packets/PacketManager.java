package xyz.devcmb.treeTumblers.packets;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {
    public static List<PacketManipulator> packetManipulators = new ArrayList<>();
    public static void RegisterPacketManipulator(PacketManipulator packetManipulator) {
        if(packetManipulator.IsDefault()){
            packetManipulator.RegisterPacketListener();
        }

        packetManipulators.add(packetManipulator);
    }

    public static void RegisterPacketManipulators(){

    }
}

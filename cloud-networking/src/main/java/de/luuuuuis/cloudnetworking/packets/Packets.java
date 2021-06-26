package de.luuuuuis.cloudnetworking.packets;

import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packets {

    public static List<Class<?>> packets = new ArrayList<>();

    public static Class<? extends Packet> getPacketClass(Class<?> packetClass) {
        if (packetClass.getClasses().length == 0) {
            return (Class<? extends Packet>) packetClass;
        }

        val packetBuf = Arrays.stream(packetClass.getClasses())
                .filter(packets -> {
                    PacketDirection.PacketDirections direction = packets.getAnnotation(PacketDirection.class).packetDirection();

                    return direction.equals(PacketDirection.PacketDirections.IN)
                            || direction.equals(PacketDirection.PacketDirections.BOTH);
                })
                .findFirst().orElse(null);
        if (packetBuf == null)
            return null;

        return (Class<? extends Packet>) packetBuf;
    }
//
//    public String getPacketName() {
//        String name;
//        if (packetClass.getAnnotation(PacketName.class) == null) {
//            name = packetClass.getSuperclass().getAnnotation(PacketName.class).name();
//        } else {
//            name = packetClass.getAnnotation(PacketName.class).name();
//        }
//        return name;
//    }
//
//    public PacketDirection.PacketDirections getPacketDirection() {
//        return packetClass.getAnnotation(PacketDirection.class).packetDirection();
//    }

}


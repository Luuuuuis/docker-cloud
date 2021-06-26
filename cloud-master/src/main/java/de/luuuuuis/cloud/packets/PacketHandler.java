package de.luuuuuis.cloud.packets;

import de.luuuuuis.cloud.packets.impl.PingPacket;
import de.luuuuuis.cloud.packets.impl.ScalePacket;
import de.luuuuuis.cloud.packets.impl.ExitPacket;
import de.luuuuuis.cloud.packets.impl.TimePackets;
import de.luuuuuis.cloudnetworking.packets.Packets;
import de.luuuuuis.cloudnetworking.server.NettyServer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    @Getter
    private final NettyServer nettyServer;
    private final List<Class<?>> packets = new ArrayList<>();

    public PacketHandler() {
        nettyServer = new NettyServer();

        packets.add(PingPacket.class);
        packets.add(ExitPacket.class);
        packets.add(TimePackets.class);
        packets.add(ScalePacket.class);

        Packets.packets.addAll(packets);
    }

}

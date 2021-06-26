package de.luuuuuis.proxy.packets;

import de.luuuuuis.proxy.packets.impl.ExitPacket;
import de.luuuuuis.proxy.packets.impl.TimePackets;
import de.luuuuuis.cloudnetworking.client.NettyClient;
import de.luuuuuis.cloudnetworking.packets.Packets;
import de.luuuuuis.proxy.packets.impl.PingPacket;
import de.luuuuuis.proxy.packets.impl.ScalePacket;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    @Getter
    private final NettyClient nettyClient;
    private final List<Class<?>> packets = new ArrayList<>();

    public PacketHandler(String host) {
        nettyClient = new NettyClient(host);

        packets.add(PingPacket.class);
        packets.add(ExitPacket.class);
        packets.add(TimePackets.class);
        packets.add(ScalePacket.class);

        Packets.packets.addAll(packets);
    }

}

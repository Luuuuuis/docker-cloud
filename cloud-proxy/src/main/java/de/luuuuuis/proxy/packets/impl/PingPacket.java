package de.luuuuuis.proxy.packets.impl;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.PacketDirection;
import de.luuuuuis.cloudnetworking.packets.PacketName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@PacketName(name = "PingPacket")
@PacketDirection(packetDirection = PacketDirection.PacketDirections.BOTH)
public class PingPacket implements Packet {

    private long time;

    @Override
    public void read(ByteBuf byteBuf) {
        time = byteBuf.readLong();
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeLong(time);
    }

    @Override
    public void handle(Channel channel) {
        System.out.println("Ping: " + (System.currentTimeMillis() - getTime()));
    }
}


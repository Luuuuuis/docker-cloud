package de.luuuuuis.proxy.packets.impl;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.PacketDirection;
import de.luuuuuis.cloudnetworking.packets.PacketName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@PacketName(name = "ExitPacket")
@PacketDirection(packetDirection = PacketDirection.PacketDirections.OUT)
public class ExitPacket implements Packet {

    @Override
    public void read(ByteBuf byteBuf) {
    }

    @Override
    public void write(ByteBuf byteBuf) {
    }

    @Override
    public void handle(Channel channel) {

    }
}

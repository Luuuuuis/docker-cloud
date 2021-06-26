package de.luuuuuis.proxy.packets.impl;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.PacketDirection;
import de.luuuuuis.cloudnetworking.packets.PacketName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@PacketName(name = "ScalePacket")
@PacketDirection(packetDirection = PacketDirection.PacketDirections.OUT)
public class ScalePacket implements Packet {

    private String params;

    public ScalePacket(String... params) {
        this.params = String.join("#", params);
    }

    @Override
    public void read(ByteBuf byteBuf) {
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byteBuf.writeCharSequence(params, StandardCharsets.UTF_8);
    }

    @Override
    public void handle(Channel channel) {
    }
}

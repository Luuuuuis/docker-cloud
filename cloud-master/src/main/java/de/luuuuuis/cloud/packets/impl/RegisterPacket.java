package de.luuuuuis.cloud.packets.impl;

import de.luuuuuis.cloud.docker.services.Service;
import de.luuuuuis.cloud.Cloud;
import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.PacketDirection;
import de.luuuuuis.cloudnetworking.packets.PacketName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@PacketName(name = "RegisterPacket")
@PacketDirection(packetDirection = PacketDirection.PacketDirections.IN)
public class RegisterPacket implements Packet {

    private String name;

    @Override
    public void read(ByteBuf byteBuf) {
        String[] strings = ((String) byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.UTF_8)).split("#");
        name = strings[0];
    }

    @Override
    public void write(ByteBuf byteBuf) {
    }

    @Override
    public void handle(Channel channel) {
        Service service = Cloud.docker.getServiceList().stream()
                .filter(serviceGroup -> name.startsWith(serviceGroup.serviceName()))
                .findFirst().orElse(null);

        assert service != null;
        service.channels.put(name, channel);
    }
}

package de.luuuuuis.proxy.packets.impl;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.PacketDirection;
import de.luuuuuis.cloudnetworking.packets.PacketName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@PacketName(name = "TimePacket")
public class TimePackets {

    @AllArgsConstructor
    @NoArgsConstructor
    @PacketDirection(packetDirection = PacketDirection.PacketDirections.IN)
    public static class InTimePacket extends TimePackets implements Packet {

        @Getter
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
            System.out.println("Time-off: " + (getTime() - System.currentTimeMillis()));
        }
    }

    @NoArgsConstructor
    @PacketDirection(packetDirection = PacketDirection.PacketDirections.OUT)
    public static class OutTimePacket extends TimePackets implements Packet {

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


}
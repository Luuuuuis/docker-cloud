package de.luuuuuis.cloudnetworking.handlers;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> output) throws Exception {
        int id = byteBuf.readInt();
        Class<? extends Packet> packetClass = Packets.getPacketClass(Packets.packets.get(id));

        assert packetClass != null;
        Packet packet = packetClass.getDeclaredConstructor().newInstance();
        packet.read(byteBuf);
        output.add(packet);
    }
}

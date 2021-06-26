package de.luuuuuis.cloudnetworking.handlers;

import de.luuuuuis.cloudnetworking.packets.Packet;
import de.luuuuuis.cloudnetworking.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf output) {
        int id = Packets.packets.indexOf(packet.getClass());

        output.writeInt(id);
        packet.write(output);
    }
}

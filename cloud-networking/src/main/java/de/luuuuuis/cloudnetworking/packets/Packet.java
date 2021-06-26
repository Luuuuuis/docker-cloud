package de.luuuuuis.cloudnetworking.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;


public interface Packet {

    void read(ByteBuf byteBuf);

    void write(ByteBuf byteBuf);

    void handle(Channel channel);

}

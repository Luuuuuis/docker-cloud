package de.luuuuuis.cloudnetworking.client;

import de.luuuuuis.cloudnetworking.handlers.NetworkHandler;
import de.luuuuuis.cloudnetworking.handlers.PacketDecoder;
import de.luuuuuis.cloudnetworking.handlers.PacketEncoder;
import de.luuuuuis.cloudnetworking.packets.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

public class NettyClient {

    @Getter
    private Channel channel;

    public NettyClient(String inetHost) {
        boolean EPOLL = Epoll.isAvailable();
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline()
                                    .addLast(new PacketDecoder())
                                    .addLast(new PacketEncoder())
                                    .addLast(new NetworkHandler());
                        }
                    }).connect(inetHost, 6969).sync().channel();

        } catch (InterruptedException e) {
            eventLoopGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NettyClient netty = new NettyClient("localhost");
        //scan(netty);
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet, channel.voidPromise());
    }

//    private static void scan(NettyClient netty) {
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            String s = scanner.next();
//            Packet packet;
//            if (s.equalsIgnoreCase("exit")) {
//                packet = new ExitPacket();
//            } else if (s.equalsIgnoreCase("ping")) {
//                packet = new PingPacket(System.currentTimeMillis());
//            } else if (s.equalsIgnoreCase("time")) {
//                packet = new TimePackets.OutTimePacket();
//            } else {
//                continue;
//            }
//
//            netty.getChannel().writeAndFlush(packet, netty.getChannel().voidPromise());
//        }
//    }
}

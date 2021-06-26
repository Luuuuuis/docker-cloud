package de.luuuuuis.cloudnetworking.server;

import de.luuuuuis.cloudnetworking.handlers.NetworkHandler;
import de.luuuuuis.cloudnetworking.handlers.PacketDecoder;
import de.luuuuuis.cloudnetworking.handlers.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public NettyServer() {
        new Thread(() -> {
            boolean EPOLL = Epoll.isAvailable();
            EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            try {
                new ServerBootstrap()
                        .group(eventLoopGroup)
                        .channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<>() {
                            @Override
                            protected void initChannel(Channel channel) {
                                channel.pipeline()
                                        .addLast(new PacketEncoder())
                                        .addLast(new PacketDecoder())
                                        .addLast(new NetworkHandler());
                            }
                        }).bind(6969).sync().channel().closeFuture().syncUninterruptibly();

            } catch (InterruptedException e) {
                eventLoopGroup.shutdownGracefully();
                e.printStackTrace();
            }
        }).start();
    }
}

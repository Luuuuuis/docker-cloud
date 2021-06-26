package de.luuuuuis.proxy.balancer;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class ReconnectBalancer implements ReconnectHandler {

    private final ConnectionBalancer connectionBalancer;

    @Override
    public ServerInfo getServer(ProxiedPlayer proxiedPlayer) {
        ServerInfo serverInfo = connectionBalancer.getReconnectServer();

        try {
            Preconditions.checkState(serverInfo != null, "Default server not found.");
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            return null;
        }

        System.out.println(proxiedPlayer.getName() + " will use reconnect server: " + serverInfo.getName());
        return serverInfo;
    }

    @Override
    public void setServer(ProxiedPlayer proxiedPlayer) {
        System.out.println("Reconnect server for " + proxiedPlayer.getName() + " is " + proxiedPlayer.getReconnectServer().getName());
    }

    @Override
    public void save() {
        System.out.println("Reconnect Server were saved");
    }

    @Override
    public void close() {
        System.out.println("Reconnect Server were closed");
    }
}

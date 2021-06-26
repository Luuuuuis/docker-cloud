package de.luuuuuis.proxy.balancer;

import de.luuuuuis.proxy.docker.updater.events.AddServerEvent;
import de.luuuuuis.proxy.docker.updater.events.RemoveServerEvent;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ConnectionBalancer implements Listener {

    @Getter
    private final Map<String, ServerInfo> fallbackServers;

    public ConnectionBalancer(ProxyServer proxyServer) {
        this.fallbackServers = new HashMap<>(proxyServer.getServers());
    }

    public ServerInfo getReconnectServer() {
        // find server with least players
        System.out.println("fallbacksServer List: " + getFallbackServers());

        return getFallbackServers().values().stream()
                .min(Comparator.comparing(o -> o.getPlayers().size()))
                .orElse(null);
    }

    @EventHandler
    public void onServerAdd(AddServerEvent event) {
        if (event.getServerInfo().getName().startsWith("Lobby.")) {
            System.out.println("Server ADD Event Called: " + event.getServerInfo().getName());
            getFallbackServers().put(event.getServerInfo().getName(), event.getServerInfo());
        }
    }

    @EventHandler
    public void onServerRemove(RemoveServerEvent event) {
        if (event.getServerInfo().getName().startsWith("Lobby.")) {
            System.out.println("Server REMOVE Event Called: " + event.getServerInfo().getName());
            getFallbackServers().remove(event.getServerInfo().getName());
        }
    }

    @EventHandler
    public void onLogin(PreLoginEvent event) {
        if (getFallbackServers().isEmpty()) {
            event.setCancelReason("No fallback server is available.");
            event.setCancelled(true);
        }
    }
}

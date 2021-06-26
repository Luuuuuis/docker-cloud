package de.luuuuuis.proxy.balancer;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class KickEvent implements Listener {

    private final ConnectionBalancer connectionBalancer;

    @EventHandler
    public void onKick(ServerKickEvent e) {
        System.out.println("kick event get called & tried to connect to reconnect server");

        ServerInfo serverInfo = connectionBalancer.getReconnectServer();

        if (serverInfo.equals(e.getKickedFrom())) {
            e.setKickReason("No fallback server could be found.");
            return;
        }

        e.setCancelled(true);
        e.setCancelServer(serverInfo);
    }


}

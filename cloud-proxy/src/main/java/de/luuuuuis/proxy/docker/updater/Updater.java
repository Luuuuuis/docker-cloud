package de.luuuuuis.proxy.docker.updater;

import de.luuuuuis.proxy.docker.inspector.ContainerEvent;
import de.luuuuuis.proxy.docker.updater.events.AddServerEvent;
import de.luuuuuis.proxy.docker.updater.events.RemoveServerEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class Updater implements Listener {

    private final ProxyServer proxyServer;
    private final String id = "GROUP";
    private final String port = "44444";
    private String motd;
    private final String restriced = "false";


    public Updater(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @EventHandler
    public void onDockerEvent(ContainerEvent event) {
        if (!event.getEnvVars().containsKey(id)) {
            return;
        }

        switch (event.getAction()) {
            case "health_status: healthy" -> addServer(event);
            case "bootstrap", "start" -> ProxyServer.getInstance().broadcast(new TextComponent("§9" + event.getName() + " is now starting."));
            case "kill", "die", "stop", "pause" -> removeServer(event);
        }
    }

    private void addServer(ContainerEvent event) {
        ServerInfo serverInfo = getServerInfoForEvent(event);

        if (serverInfo.getAddress().getHostName() == null) {
            System.err.println("Could not add Server " + serverInfo.getName() + " cause the instance has no Ip??");
            return;
        }

        if (proxyServer.getServers().containsKey(serverInfo.getName())) {
            System.err.println("Server with id " + serverInfo.getName() + " already exists");

            InetSocketAddress currentAddress = proxyServer.getServers().get(serverInfo.getName()).getAddress();

            if (!currentAddress.equals(serverInfo.getAddress())) {
                proxyServer.getServers().remove(serverInfo.getName());
                System.err.println("Ip changed so remove server to add him later");
            }
        }

        proxyServer.getServers().put(serverInfo.getName(), serverInfo);
        System.out.println("Server added " + serverInfo.getName());
        ProxyServer.getInstance().broadcast(new TextComponent("§a" + event.getName() + " is now online!"));

        // Call Event for server added
        proxyServer.getPluginManager().callEvent(new AddServerEvent(serverInfo));
    }

    private void removeServer(ContainerEvent event) {
        String id = getServerId(event);

        if (!proxyServer.getServers().containsKey(id)) {
            System.err.println("Could not remove server " + id + " because it does not exist");
            return;
        }

        // Call event for server removed
        proxyServer.getPluginManager().callEvent(new RemoveServerEvent(getServerInfoForEvent(event)));

        proxyServer.getServers().remove(id);
        ProxyServer.getInstance().broadcast(new TextComponent("§4" + event.getName() + " is now offline!"));

    }

    private ServerInfo getServerInfoForEvent(ContainerEvent eventData) {
        String id = getServerId(eventData);

        // Getting the address to create
        int port = eventData.getEnvVars().get(this.port) != null
                ? Integer.parseInt(eventData.getEnvVars().get(this.port))
                : (eventData.getPort() != null ? eventData.getPort() : 25565);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(eventData.getIp(), port);


        // Getting the motd
        String motd = eventData.getEnvVars().get(this.motd) != null
                ? eventData.getEnvVars().get(this.motd)
                : "A Minecraft Server Instance";

        // Getting restricted bool
        boolean restricted =
                eventData.getEnvVars().get(this.restriced) != null &&
                        eventData.getEnvVars().get(this.restriced).equals("restricted");

        return ProxyServer.getInstance().constructServerInfo(
                id,
                inetSocketAddress,
                motd,
                restricted
        );
    }

    private String getServerId(ContainerEvent eventData) {
        if (eventData.getName() != null) {
            return eventData.getName().replace("/", "");
        }

        return eventData.getId();
    }
}

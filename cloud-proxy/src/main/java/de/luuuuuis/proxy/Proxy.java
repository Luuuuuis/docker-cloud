package de.luuuuuis.proxy;

import de.luuuuuis.proxy.balancer.ConnectionBalancer;
import de.luuuuuis.proxy.balancer.HubCommand;
import de.luuuuuis.proxy.balancer.KickEvent;
import de.luuuuuis.proxy.balancer.ReconnectBalancer;
import de.luuuuuis.proxy.commands.PingCommand;
import de.luuuuuis.proxy.commands.ScaleCommand;
import de.luuuuuis.proxy.packets.PacketHandler;
import de.luuuuuis.proxy.docker.Docker;
import de.luuuuuis.proxy.docker.inspector.Inspector;
import de.luuuuuis.proxy.docker.updater.Updater;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class Proxy extends Plugin {

    private Docker docker;
    private PacketHandler packetHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        System.out.println("hello there!");

        docker = new Docker("unix:///var/run/docker.sock");
        //docker = new Docker("tcp://host.docker.internal:2375");
        packetHandler = new PacketHandler("172.17.0.1");
        //packetHandler = new PacketHandler("host.docker.internal");

        bootstrapContainerInspector();
        bootstrapServerUpdater();
        bootstrapBalancer();

        getProxy().getPluginManager().registerCommand(this, new PingCommand());
        getProxy().getPluginManager().registerCommand(this, new ScaleCommand(this));
    }

    private void bootstrapContainerInspector() {
        getProxy().getConfig().getServers().clear();

        Inspector inspector = new Inspector(this.getProxy(), docker.getDockerClient());

        getProxy().getScheduler().runAsync(this, inspector::runContainerInspection);
        getProxy().getScheduler().runAsync(this, inspector::runContainerListener);
    }

    private void bootstrapServerUpdater() {
        Updater updater = new Updater(getProxy());
        getProxy().getPluginManager().registerListener(this, updater);
    }

    private void bootstrapBalancer() {
        ConnectionBalancer connectionBalancer = new ConnectionBalancer(getProxy());

        getProxy().setReconnectHandler(new ReconnectBalancer(connectionBalancer));

        getProxy().getPluginManager().registerListener(this, connectionBalancer);
        getProxy().getPluginManager().registerListener(this, new KickEvent(connectionBalancer));
        getProxy().getPluginManager().registerCommand(this, new HubCommand(connectionBalancer));
    }
}

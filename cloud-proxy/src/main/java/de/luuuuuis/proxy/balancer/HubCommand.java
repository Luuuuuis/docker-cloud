package de.luuuuuis.proxy.balancer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    private final ConnectionBalancer connectionBalancer;

    public HubCommand(ConnectionBalancer connectionBalancer) {
        super("hub", null, "lobby", "l");
        this.connectionBalancer = connectionBalancer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            System.err.println("Your not a fuking player, mate");
            return;
        }

        if (player.getServer().getInfo().getName().contains("Lobby")) {
            player.sendMessage(new TextComponent("Your already on a lobby server"));
            return;
        }

        ServerInfo serverInfo = connectionBalancer.getReconnectServer();
        player.connect(serverInfo);
    }
}

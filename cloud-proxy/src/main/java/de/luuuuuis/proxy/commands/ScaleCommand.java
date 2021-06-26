package de.luuuuuis.proxy.commands;

import de.luuuuuis.proxy.packets.impl.ScalePacket;
import de.luuuuuis.proxy.Proxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ScaleCommand extends Command {

    private final Proxy proxy;

    public ScaleCommand(Proxy proxy) {
        super("scale");
        this.proxy = proxy;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sender.sendMessage(new TextComponent("§c/scale GROUP COUNT"));
            return;
        }

        String group = strings[0];
        String count = strings[1];

        proxy.getPacketHandler().getNettyClient().sendPacket(new ScalePacket(group, count));
        sender.sendMessage(new TextComponent("§aScaled service " + group + " to " + count));
    }
}

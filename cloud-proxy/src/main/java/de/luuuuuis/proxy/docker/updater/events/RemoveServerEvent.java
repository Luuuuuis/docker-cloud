package de.luuuuuis.proxy.docker.updater.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Event;

@EqualsAndHashCode(callSuper = true)
@Data
public class RemoveServerEvent extends Event {

    private final ServerInfo serverInfo;

}
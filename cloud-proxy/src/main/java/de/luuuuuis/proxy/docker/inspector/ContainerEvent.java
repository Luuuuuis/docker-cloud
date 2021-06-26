package de.luuuuuis.proxy.docker.inspector;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.plugin.Event;

import java.net.InetAddress;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContainerEvent extends Event {

    private String id, action, name;
    private InetAddress ip;
    private Integer port;
    private Map<String, String> envVars;

    public ContainerEvent(String id, String action) {
        this.id = id;
        this.action = action;
    }
}

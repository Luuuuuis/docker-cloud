package de.luuuuuis.cloud;

import de.luuuuuis.cloud.packets.PacketHandler;
import de.luuuuuis.cloud.docker.Docker;

import java.util.Scanner;

public class Cloud {

    public static Docker docker;
    public static PacketHandler packetHandler;

    public Cloud() {
        docker = new Docker();

        packetHandler = new PacketHandler();

        scan();
    }

    public static void main(String[] args) {
        new Cloud();
    }

    private void scan() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.next();
            if (s.equalsIgnoreCase("exit")) {
                System.exit(1);
            } else if (s.equalsIgnoreCase("scale")) {
                docker.getServiceList().get(0).updateService(2);
            }
        }
    }

}

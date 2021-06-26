# Docker Cloud
Minecraft Cloud system implemented in Docker with Docker Swarm

!This is a non-functioning prototype and just a test with Docker Swarm and Netty!

Create docker api certs: https://gist.github.com/kekru/974e40bb1cd4b947a53cca5ba4b0bbe5

Docker BungeeCord implementation inspired by https://github.com/DockerizedCraft/Core

Known bugs:
 - Slow startup of containers on windows
 - Docker Events are not being broadcasted over multiple dedicated servers -> No servers on different dedicated servers can be discovered
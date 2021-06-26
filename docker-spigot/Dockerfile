FROM openjdk:16-alpine3.13


COPY ./* /

EXPOSE 25565/tcp
EXPOSE 25565/udp

HEALTHCHECK --start-period=5m CMD /health.sh

CMD ["java", "-jar", "-Xmx2G", "paper-1.16.5-776.jar"]

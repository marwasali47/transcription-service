FROM dockerproxy-iva.si.francetelecom.fr/openjdk:8-slim

ARG JAR_FILE

ENV ACTIVE_PROFILE ""

EXPOSE 4444

ADD ${JAR_FILE} /opt/hubme/transcription-service.jar

WORKDIR /opt/hubme

RUN chown -R 1003540000:1003540000 /opt/hubme

USER 1003540000

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${ACTIVE_PROFILE} \
                            -Dhttp.proxyHost=cs.pr-proxy.service.sd.diod.tech \
                            -Dhttps.proxyHost=cs.pr-proxy.service.sd.diod.tech \
                            -Dhttp.proxyPort=3128 \
                            -Dhttps.proxyPort=3128 \
                            -Dhttp.nonProxyHosts=\"localhost|127.0.0.1|*.local\" \
                            -jar transcription-service.jar"]

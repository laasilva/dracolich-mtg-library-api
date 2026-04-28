FROM eclipse-temurin:25-jre-noble
LABEL authors="laasilva" \
      description="dracolich-mtg-library-api" \
      version="1.0" \
      org.opencontainers.image.vendor="dracolich" \
      org.opencontainers.image.title="Dracolich MTG Library API"

EXPOSE 8080
EXPOSE 7980

RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends ca-certificates curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN useradd -r -s /bin/false -U -d /opt/mtg-library appuser \
    && mkdir -p /opt/mtg-library \
    && chown -R appuser:appuser /opt/mtg-library

USER appuser
WORKDIR /opt/mtg-library

COPY --chown=appuser:appuser mtg-library-api-web/target/mtg-library-api-web.jar /opt/mtg-library/mtg-library.jar

HEALTHCHECK --interval=60s --timeout=5s --start-period=120s --retries=3 \
    CMD curl -fsS http://localhost:7980/actuator/health || exit 1

ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "/opt/mtg-library/mtg-library.jar"]
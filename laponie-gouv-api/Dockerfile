FROM alpine:3.19.1

ARG APP_FILE
EXPOSE 8080

WORKDIR /app

COPY target .
CMD ["ls", "-la", "/app"]

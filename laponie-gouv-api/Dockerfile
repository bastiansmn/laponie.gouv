FROM ubuntu:jammy

ARG APP_FILE
EXPOSE 8080

COPY target/$APP_FILE app
ENTRYPOINT ["./app"]

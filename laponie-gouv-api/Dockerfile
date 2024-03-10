FROM alpine:3.19.1

ARG APP_FILE
EXPOSE 8080

COPY target/laponie-gouv-api /laponie-gouv-api
ENTRYPOINT ["/laponie-gouv-api"]
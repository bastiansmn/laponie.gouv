FROM alpine:3.19.1

ARG APP_FILE
EXPOSE 8080

COPY target/$APP_FILE /app
CMD ["./app"]
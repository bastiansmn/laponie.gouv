#!/bin/bash

SECRET_NAME="laponie-gouv-secret"

export $(sops -d secret.env | xargs)

kubectl -n=laponie-gouv delete secret ${SECRET_NAME} 2> /dev/null
kubectl -n=laponie-gouv \
create secret generic ${SECRET_NAME} \
--from-literal="SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL" \
--from-literal="SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME" \
--from-literal="SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD" \
--from-literal="POSTGRES_USER=$SPRING_DATASOURCE_USERNAME" \
--from-literal="POSTGRES_PASSWORD=$SPRING_DATASOURCE_PASSWORD" \
--from-literal="SPRING_MAIL_HOST=$SPRING_MAIL_HOST" \
--from-literal="SPRING_MAIL_PORT=$SPRING_MAIL_PORT" \
--from-literal="SPRING_MAIL_USERNAME=$SPRING_MAIL_USERNAME" \
--from-literal="SPRING_MAIL_PASSWORD=$SPRING_MAIL_PASSWORD"

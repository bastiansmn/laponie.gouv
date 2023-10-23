#!/bin/bash

SECRET_NAME="laponie-gouv-secret"

export $(sops -d secret.env | xargs)

kubectl -n=laponie-gouv delete secret ${SECRET_NAME} 2> /dev/null
kubectl -n=laponie-gouv \
create secret generic ${SECRET_NAME} \
--from-literal="DB_USERNAME=$DB_USERNAME" \
--from-literal="DB_PASSWORD=$DB_PASSWORD" \
--from-literal="DB_URL=$DB_URL" \
--from-literal="DB_NAME=$DB_NAME" \
--from-literal="POSTGRES_PASSWORD=$DB_PASSWORD" \
--from-literal="POSTGRES_USER=$DB_USER" \
--from-literal="POSTGRES_DB=$DB_NAME" \
--from-literal="SMTP_HOST=$SMTP_HOST" \
--from-literal="SMTP_PORT=$SMTP_PORT" \
--from-literal="SMTP_USERNAME=$SMTP_USERNAME" \
--from-literal="SMTP_PASSWORD=$SMTP_PASSWORD"

FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.27.2-alpine
RUN rm -rf /usr/share/nginx/html/* && \
    rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/laponie-gouv-client/browser /usr/share/nginx/html

# ---- Stage 1: build ----
FROM node:20 AS build
WORKDIR /app

# Copiamo prima solo i file delle dipendenze, cosi' Docker riusa la cache
# se cambia solo il codice sorgente e non le dipendenze
COPY package*.json ./
RUN npm install

# Ora copiamo il resto del codice sorgente e buildiamo per l'ambiente Docker
COPY . .
RUN npm run build -- --configuration=docker

# ---- Stage 2: runtime (Nginx serve i file statici gia' pronti) ----
FROM nginx:alpine
COPY --from=build /app/dist/hackhub-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
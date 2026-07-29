export const environment = {
    production: true,
    // in Docker il frontend chiama il backend tramite il nome del servizio
    // definito in docker-compose.yml, non tramite "localhost"
    apiUrl: 'http://hackhub:8080'
};
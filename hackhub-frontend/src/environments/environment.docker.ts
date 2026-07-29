export const environment = {
    production: true,
    // Il browser dell'utente NON fa parte della rete Docker interna:
    // deve chiamare il backend tramite la porta pubblicata sull'host,
    // non tramite il nome del servizio (quello vale solo tra container,
    // es. backend -> mysql).
    apiUrl: 'http://localhost:8080'
};
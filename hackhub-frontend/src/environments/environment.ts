export const environment = {
    production: false,
    // Vuoto: le chiamate usano percorsi relativi (es. "/hackathon/all"),
    // che il proxy di Angular (proxy.conf.json) inoltra al backend su :8080
    // facendoli sembrare, per il browser, della stessa origine.
    apiUrl: ''
};
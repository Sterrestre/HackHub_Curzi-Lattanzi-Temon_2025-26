export const environment = {
    production: true,
    // Calcola l'indirizzo del backend usando lo stesso host con cui e' stato
    // aperto il frontend. Cosi' funziona sia in locale (localhost) sia su
    // AWS (l'IP pubblico) sia con un eventuale dominio futuro, senza dover
    // ricompilare per ogni ambiente diverso.
    apiUrl: `http://${window.location.hostname}:8080`
};
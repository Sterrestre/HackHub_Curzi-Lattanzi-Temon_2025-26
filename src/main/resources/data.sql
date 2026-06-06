-- =============================================
-- UTENTI DI TEST
-- =============================================

-- Organizzatore
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-org-001', 'Luca', 'Bianchi', 'luca.bianchi@example.com', 'lcbianchi', 'Organizzatore di hackathon', '1985-03-20', TRUE);

-- Giudice
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-giu-001', 'Anna', 'Verdi', 'anna.verdi@example.com', 'annaverdi', 'Giudice esperta', '1980-07-15', TRUE);

-- Mentore
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-men-001', 'Carlo', 'Neri', 'carlo.neri@example.com', 'carloneri', 'Mentore tecnico', '1978-11-10', TRUE);

-- Partecipante 1 (admin team)
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-par-001', 'Mario', 'Rossi', 'mario.rossi@example.com', 'mrossi', 'Sviluppatore', '1995-05-22', FALSE);

-- Partecipante 2
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-par-002', 'Sara', 'Russo', 'sara.russo@example.com', 'srusso', 'Designer', '1997-09-18', FALSE);

-- Partecipante 3
MERGE INTO UTENTE (UTENTEID, UTENTE_NOME, UTENTE_COGNOME, UTENTE_EMAIL, NICKNAME, BIOGRAFIA, DATA_DI_NASCITA, MEMBRO_DI_STAFF)
    KEY(UTENTEID) VALUES ('utente-par-003', 'Giulia', 'Ferrari', 'giulia.ferrari@example.com', 'gferrari', 'Data scientist', '1996-01-30', FALSE);

-- =============================================
-- HACKATHON CONFERMATO
-- =============================================

MERGE INTO HACKATHON (ID, NOME, REGOLAMENTO, DATA_INIZIO, DATA_FINe, SCADENZA_ISCRIZIONI, LUOGO, QUOTA_ISCRIZIONE, PREMIO, DIM_MAX_TEAM, NUM_MAX_TEAM, STATO, CLASSIFICA_CONFERMATA, NUM_TEAM_ISCRITTI)
    KEY(ID) VALUES (
    'hack-001',
    'HackHub 2026',
    'Ogni team deve sviluppare un progetto innovativo in 48 ore.',
    '2026-08-01 09:00:00',
    '2026-08-03 18:00:00',
    '2026-07-15 23:59:00',
    'Milano',
    0.0,
    5000.0,
    4,
    10,
    'CONFERMATO',
    FALSE,
    0
    );

-- =============================================
-- RUOLI STAFF HACKATHON
-- DTYPE corrisponde al nome della sottoclasse JPA
-- =============================================

-- Organizzatore
MERGE INTO RUOLO_PARTECIPAZIONE (ID, DTYPE, UTENTE_UTENTEID, HACKATHON_ID)
    KEY(ID) VALUES ('ruolo-org-001', 'ORGANIZZATORE', 'utente-org-001', 'hack-001');

MERGE INTO RUOLO_PARTECIPAZIONE (ID, DTYPE, UTENTE_UTENTEID, HACKATHON_ID)
    KEY(ID) VALUES ('ruolo-giu-001', 'GIUDICE', 'utente-giu-001', 'hack-001');

MERGE INTO RUOLO_PARTECIPAZIONE (ID, DTYPE, UTENTE_UTENTEID, HACKATHON_ID)
    KEY(ID) VALUES ('ruolo-men-001', 'MENTORE', 'utente-men-001', 'hack-001');

-- =============================================
-- TEAM
-- =============================================

MERGE INTO TEAM (TEAMID, NOME)
    KEY(TEAMID) VALUES ('team-001', 'Team Alpha');

-- Membro admin del team
MERGE INTO MEMBRO_TEAM (MEMBRO_TEAM_ID, AMMINISTRATORE, TEAM_TEAMID, UTENTE_UTENTEID)
    KEY(MEMBRO_TEAM_ID) VALUES ('membro-001', TRUE, 'team-001', 'utente-par-001');

-- Secondo membro
MERGE INTO MEMBRO_TEAM (MEMBRO_TEAM_ID, AMMINISTRATORE, TEAM_TEAMID, UTENTE_UTENTEID)
    KEY(MEMBRO_TEAM_ID) VALUES ('membro-002', FALSE, 'team-001', 'utente-par-002');
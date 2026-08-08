package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.InfoHackBuilderImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test di integrazione con database H2 in-memory (@DataJpaTest sostituisce
 * automaticamente la configurazione MySQL con un H2 usa-e-getta, grazie alla
 * dipendenza H2 presente in "testRuntimeOnly").
 * Verifica che l'entita' Hackathon (con il suo campo @Embedded InfoHack)
 * venga salvata e riletta correttamente.
 */
@DataJpaTest
class HackathonRepositoryTest {

    @org.springframework.beans.factory.annotation.Autowired
    private HackathonRepository hackathonRepository;

    @Test
    void unHackathonSalvatoVieneRitrovatoConGliStessiDati() {
        InfoHack info = new InfoHackBuilderImpl()
                .regolamento("Regolamento di prova")
                .luogo("Camerino")
                .dataInizio(LocalDateTime.now().plusDays(30))
                .dataFine(LocalDateTime.now().plusDays(35))
                .scadenzaIscrizioni(LocalDateTime.now().plusDays(20))
                .quotaIscrizione(0)
                .premio(500)
                .dimMaxTeam(4)
                .numMaxTeam(8)
                .build();

        Hackathon hackathon = new Hackathon(info, "Hackathon di prova");

        hackathonRepository.save(hackathon);

        Optional<Hackathon> ritrovato = hackathonRepository.findById(hackathon.getHackathonID());

        assertThat(ritrovato).isPresent();
        assertThat(ritrovato.get().getNome()).isEqualTo("Hackathon di prova");
        assertThat(ritrovato.get().getInfoHack().getLuogo()).isEqualTo("Camerino");
    }
}
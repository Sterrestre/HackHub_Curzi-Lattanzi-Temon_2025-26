package it.unicam.cs.ids.model;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

public class Sottomissione {
    private String sottomissioneID;
    private File file;
    private StatoSottomissione statoSottomissione = StatoSottomissione.MANCANTE;
    private Valutazione valutazione;
    private LocalDateTime dataCaricamento;


    public Sottomissione(String sottomissioneID) {
        if (sottomissioneID == null) {
            throw new IllegalArgumentException("L'ID della sottomissione non può essere null.");
        }
        this.sottomissioneID = sottomissioneID;
    }

    public String getSottomissioneID() {
        return sottomissioneID;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Il file della sottomissione non può essere null.");
        }
        this.file = file;
    }

    public StatoSottomissione getStatoSottomissione() {
        return statoSottomissione;
    }

    public void setStatoSottomissione(StatoSottomissione statoSottomissione) {
        if (statoSottomissione == null) {
            throw new IllegalArgumentException("Lo stato della sottomissione non può essere null.");
        }
        this.statoSottomissione = statoSottomissione;
    }
    public LocalDateTime getDataCaricamento(){
        return dataCaricamento;
    }

    public void setDataCaricamento(LocalDateTime dataCaricamento){
        if (dataCaricamento == null) {
            throw new IllegalArgumentException("La data di caricamento della sottomissione non può essere null.");
        }
        this.dataCaricamento = dataCaricamento;
    }
    public Valutazione getValutazione() {
        return valutazione;
    }
    public void setValutazione(Valutazione valutazione) {
        if (valutazione == null) {
            throw new IllegalArgumentException("La valutazione della sottomissione non può essere null.");
        }
        this.valutazione = valutazione;
    }

     public boolean isValutata() {
        return statoSottomissione == StatoSottomissione.VALUTATA;
   }

   @Override
    public boolean equals(Object o) {
        if(!(o instanceof Sottomissione other)) {
            return false;
        }
        return Objects.equals(sottomissioneID, other.sottomissioneID);
   }

    @Override
    public int hashCode() {
        return Objects.hash(sottomissioneID);
    }


    @Override
    public String toString() {
        return "Sottomissione{id='" + sottomissioneID + "', stato=" + statoSottomissione + ", data=" + dataCaricamento + "}";
    }

 /** DA RIVEDERE
*/
//   public String getGiudizio() {
//        if (valutazione == null) {
//            throw new IllegalStateException("Impossibile ottenere il giudizio: sottomissione non valutata");
//        }
//        return valutazione.getGiudizio();
//    }
//
//    public long getId() {
//        return 0;
//    }

}
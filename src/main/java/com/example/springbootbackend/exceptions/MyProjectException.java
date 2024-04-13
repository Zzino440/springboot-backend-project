package com.example.springbootbackend.exceptions;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class MyProjectException extends RuntimeException {

    private final MyProjectError myProjectError;
    @Nullable
    private String optionalDescription;


    /**
     * Recupera la descrizione effettiva dell'eccezione.
     * Questo metodo determina quale messaggio di errore dovrebbe essere visualizzato
     * quando un'istanza di MyProjectException viene gestita. Se è stata fornita una
     * descrizione opzionale e questa non è vuota, tale descrizione verrà utilizzata come
     * messaggio di errore. Altrimenti, verrà usata la descrizione di default associata
     * all'errore di progetto specificato al momento della creazione dell'eccezione.
     * Questo approccio permette una maggiore flessibilità nel fornire feedback agli utenti
     * o ai sistemi che interagiscono con l'applicazione, permettendo di specificare messaggi
     * di errore personalizzati in determinati contesti, pur mantenendo un set predefinito
     * di messaggi di errore ben definiti per gli scenari più comuni.
     *
     * @return La descrizione effettiva dell'eccezione, che può essere la descrizione opzionale
     *         personalizzata se presente e non vuota, altrimenti la descrizione predefinita
     *         associata all'errore di progetto.
     */
    public String getEffectiveDescription() {
        return (optionalDescription == null || optionalDescription.isBlank())
                ? myProjectError.getDescription()
                : optionalDescription;
    }



}

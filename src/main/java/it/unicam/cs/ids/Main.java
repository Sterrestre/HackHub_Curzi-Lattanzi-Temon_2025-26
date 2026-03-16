package it.unicam.cs.ids;

import it.unicam.cs.ids.service.InvitiHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public class InvitoScheduler {

        public InvitoScheduler(InvitiHandler handler) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(handler::verificaScadenze, 0, 1, TimeUnit.MINUTES);
        }
    }

}
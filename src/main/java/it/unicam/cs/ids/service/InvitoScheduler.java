package it.unicam.cs.ids.service;

import it.unicam.cs.ids.controller.InvitiHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InvitoScheduler {

    public InvitoScheduler(InvitiHandler handler) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(handler::verificaScadenze, 0, 1, TimeUnit.MINUTES);
    }
}
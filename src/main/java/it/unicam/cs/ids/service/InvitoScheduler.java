package it.unicam.cs.ids.service;

import it.unicam.cs.ids.controller.InvitiHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class InvitoScheduler {

    private final InvitiHandler invitiHandler;

    public InvitoScheduler(InvitiHandler invitiHandler) {
        this.invitiHandler = invitiHandler;
    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        invitiHandler.verificaScadenze();
    }
}

//public class InvitoScheduler {
//
//    public InvitoScheduler(InvitiHandler handler) {
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.scheduleAtFixedRate(handler::verificaScadenze, 0, 1, TimeUnit.MINUTES);
//    }
//}
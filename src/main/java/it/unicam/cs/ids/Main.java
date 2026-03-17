package it.unicam.cs.ids;

import it.unicam.cs.ids.service.infrastructure.Gmail;
import it.unicam.cs.ids.service.infrastructure.GmailClientFactory;
import it.unicam.cs.ids.service.infrastructure.GmailMailSender;
import it.unicam.cs.ids.service.InvitiHandler;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.NotificationService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    Gmail gmail = GmailClientFactory.createGmailClient();
    MailSender mailSender = new GmailMailSender(gmail);
    NotificationService notificationService = new NotificationService(mailSender);
    private GmailMailSender invitoService;
    InvitiHandler handler = new InvitiHandler(invitoService, notificationService);

    public Main() throws Exception {
    }

    public class InvitoScheduler {

        public InvitoScheduler(InvitiHandler handler) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(handler::verificaScadenze, 0, 1, TimeUnit.MINUTES);
        }
    }

}
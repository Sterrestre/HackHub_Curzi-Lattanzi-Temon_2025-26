package it.unicam.cs.ids;

import it.unicam.cs.ids.controller.InvitiHandler;
import it.unicam.cs.ids.model.RoleFactory;
import it.unicam.cs.ids.service.*;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailClientFactory;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailMailSender;
import com.google.api.services.gmail.Gmail;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Creazione client Gmail e RoleFactory
        Gmail gmail = GmailClientFactory.createGmailClient();
        RoleFactory roleFactory = new RoleFactory();


        // 2. Adapter per inviare email
        MailSender mailSender = new GmailMailSender(gmail);

        // 3. Servizio applicativi
        InvitoService invitoService = new InvitoService();
        NotificationService notificationService = new NotificationService(mailSender);

        // 4. Handler dei casi d'uso
        InvitiHandler invitiHandler = new InvitiHandler(invitoService, notificationService, roleFactory);

        // 5. Scheduler
        new InvitoScheduler(invitiHandler);
    }
}

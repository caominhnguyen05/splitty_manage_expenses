package server.api;

import commons.Event;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.MailSenderService;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailSenderService senderService;

    /**
     * Send an email formatted with HTML
     * @param event event to which the person is invited
     * @param email email of the recipient
     * @return return http 200
     */
    @PostMapping("/{email}")
    public ResponseEntity<Event> sendHtml(@RequestBody Event event,
                                          @PathVariable("email") String email) {
        System.out.println("request received");
        if (event.name == null || event.name.isEmpty() ) {
            System.out.println("Event is null");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Sending email");
        senderService.htmlMail(event, email);

        return ResponseEntity.status(200).build();
    }

    /**
     * sends an email as a payment reminder
     * @param participant
     * @param email
     * @return return http 200
     */
    @PostMapping("/reminder/{email}")
    public ResponseEntity<Event> sendReminder(@RequestBody Participant participant,
                                              @PathVariable("email") String email) {
        System.out.println("request received");
        if (participant==null || email==null || email.isEmpty()) {
            System.out.println("Debt is null");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Sending email");
        senderService.reminderEmail(participant, email);

        return ResponseEntity.status(200).build();
    }

    /**
     * sends an email as join event confirmation
     * @param event
     * @param email
     * @return return http 200
     */
    @PostMapping("/confirmation/{email}")
    public ResponseEntity<Event> sendConfirmation(@RequestBody Event event,
                                                  @PathVariable("email") String email){
        System.out.println("request received");
        if(event == null || event.name == null ||
            email == null || email.isEmpty()){
            System.out.println("Wrong data");
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Sending email");
        senderService.confirmationEmail(event, email);

        return ResponseEntity.status(200).build();
    }

    /**
     * sends an email as credentials update confirmation
     * @param participant
     * @param email
     * @return return http 200
     */
    @PostMapping("/confirmation/update/{email}")
    public ResponseEntity<Event> sendConfirmationUpdate(@RequestBody Participant participant,
                                                        @PathVariable("email") String email){
        System.out.println("request received");
        if(email == null || email.isEmpty()){
            System.out.println("Wrong data");
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Sending email");
        senderService.confirmationUpdateEmail(participant,email);

        return ResponseEntity.status(200).build();
    }
}
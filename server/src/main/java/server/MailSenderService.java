package server;

import commons.Event;
import commons.Participant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailSenderService {

    private static final String fromEmail = "splitty650@gmail.com";
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send a simple (.txt) mail
     * @param toEmail recipient mail address
     * @param subject subject of the mail
     * @param body textual body of the mail
     */
    public void simpleMail(String toEmail,
                          String subject,
                          String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Mail sent?");
    }

    /**
     * Send an email with an attachment
     * @param toEmail recipient mail address
     * @param subject subject of the mail
     * @param body textual body of the mail
     * @param pathToAttachment path to attachment file
     */
    public void attachmentMail(String toEmail,
                               String subject,
                               String body,
                               String pathToAttachment) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        try {
            helper.addAttachment("Attachment", file);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }

    /**
     * Send an email with a pre-determined HTML layout
     * @param event event for the person who is invited
     * @param toEmail recipient email address
     */
    public void htmlMail(Event event, String toEmail) {

        System.out.println("Sending HTML mail");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);

            helper.setSubject(String.format("Invited to join %s!", event.name));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        String htmlContent = String.format("<h1>You are invited to join %s!</h1>", event.name) +
                String.format("" +
                        "<p>You can enter the code <b style=\"font-size:16px;\">%s</b>" +
                        " in Splitty to join this event.", event.id) +
                "<p>This email was automatically sent by Splitty, " +
                "after your friend invited you.</p>";

        try {
            message.setContent(htmlContent, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }

    /**
     * Send an email with a pre-determined HTML layout, containing a payment reminder
     * @param participant
     * @param toEmail
     */
    public void reminderEmail(Participant participant, String toEmail){
        System.out.println("Sending payment reminder email");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.addCc(participant.email);
            helper.setSubject("Payment reminder");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        String reminderContent = String.format("<h1>%s sent you a payment " +
            "reminder!</h1>", participant.name) +
            "<p> More details about this payment can be found " +
            "in the detailed debt page.</p>"+
            "<p>This email was automatically sent by Splitty, " +
            "at your friend's request!</p>";
        try {
            message.setContent(reminderContent, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    /**
     * Send a confirmation email to the participant's address
     * after he/she has been added
     * @param toEmail
     * @param event
     */
    public void confirmationEmail(Event event, String toEmail){
        System.out.println("Sending confirmation email");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try{
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Register Confirmation!");
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        String body = String.format("<h1> You have been added to %s</h1>", event.name)+
            "<p> Your registration was successful and your credentials " +
            "are correct.</p>"+
            "<p>This email was automatically sent by Splitty!</p>";
        try {
            message.setContent(body, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    /**
     * Send a confirmation email to the participant's address
     * after his/her credentials have been updated
     * @param participant
     * @param toEmail
     */
    public void confirmationUpdateEmail(Participant participant,String toEmail){
        System.out.println("Sending confirmation email");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try{
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Register Confirmation!");
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        String body = "<h1> Your credentials have updated!</h1>"+
            "<p> This is your data now:</p>"+
            String.format("Name: %s <br>"+
                "Email: %s <br>"+
                "IBAN: %s <br>"+
                "BIC: %s",participant.name,participant.email,
                participant.iban,participant.bic)+
            "<p>This email was automatically sent by Splitty!</p>";
        try {
            message.setContent(body, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

}
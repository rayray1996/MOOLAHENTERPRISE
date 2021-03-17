/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.email;

import ejb.entity.CompanyEntity;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author nickg
 */
public class EmailManager {

    private final String emailServerName = "smtp.gmail.com";
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;

    public EmailManager() {
    }

    public EmailManager(String smtpAuthUser, String smtpAuthPassword) {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }

    public Boolean emailCreditTopupNotification(CompanyEntity company, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "";
//        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ");
        LocalDateTime currDate = LocalDateTime.now();
        String dateFormattedcurrDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(currDate);

        LocalDateTime expiryDate = currDate.plusWeeks(1);
        String dateFormattedexpiryDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(expiryDate);

//        String dateFormat = currDate.format(fmt);
//        String dateFormattedcurrDate = fmt.format(currDate.toString());
//        String dateFormattedexpiryDate = fmt.format(expiryDate.toString());
        emailBody += "Dear " + company.getCompanyName() + ", \n\n";
        emailBody += "You currently have insufficient credit.\n\nCurrent credit amount as of " + dateFormattedcurrDate + " is : " + company.getCreditOwned() + "\n\n";
        emailBody += "Please top it up by " + dateFormattedexpiryDate + ". \n\n\n";
        emailBody += "Yours Sincerely, \nMoolah Enterprise";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Insufficient Credit for Moolah Enterprise!");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Boolean emailReminderAccountDeactivated(CompanyEntity company, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "";
//        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ");
//        LocalDateTime currDate = LocalDateTime.now();
//        LocalDateTime expiryDate = currDate.plusMonths(6);

//        String dateFormattedexpiryDate = fmt.format(expiryDate);
        LocalDateTime currDate = LocalDateTime.now();

        LocalDateTime expiryDate = currDate.plusWeeks(1);
        String dateFormattedexpiryDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(expiryDate);

        emailBody += "Dear " + company.getCompanyName() + ", \n\n";
        emailBody += "You currently have insufficient credit!\n\nDue to lack of action, we will be deactivating your account." + "\n\n";
        emailBody += "Please top it up by " + dateFormattedexpiryDate + ". \n\n\n";
        emailBody += "Yours Sincerely, \nMoolah Enterprise";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Account Deactivation for Moolah Enterprise");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}

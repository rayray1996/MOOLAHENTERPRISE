/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.email;

import ejb.entity.CompanyEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.MonthlyPaymentEntity;
import ejb.entity.ProductLineItemEntity;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Calendar;
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
                msg.setSubject("Insufficient Credit for Moolah Enterprise");
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

    public Boolean emailMonthlyPaymentInvoice(MonthlyPaymentEntity monthlyPaymentEntity, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "";

        SimpleDateFormat format = new SimpleDateFormat("MMM-YYYY");
        String currentMonth = format.format(monthlyPaymentEntity.getDateGenerated().getTime());

        emailBody += "Dear " + monthlyPaymentEntity.getCompany().getCompanyName() + ",<br/><br/>";
        emailBody += "Here is the monthly invoice for the Month of " + currentMonth + ". <br/><br/><br/><br/>";

        emailBody
                = emailBody + "<html><body><table width='100%' border='1' align='center'>"
                + "<tr align='center'>"
                + "<td><b>S/N <b></td>"
                + "<td><b>Product Name<b></td>"
                + "<td><b>Monthly Clicks<b></td>"
                + "<td><b>Sub-Total (Credits)<b></td>"
                + "</tr>";

        int count = 0;

        for (ProductLineItemEntity prodLineItem : monthlyPaymentEntity.getListOfProductLineItems()) {
            count++;
            emailBody = emailBody + "<tr align='center'>" + "<td>" + count + "</td>"
                    + "<td>" + prodLineItem.getProduct().getProductName() + "</td>"
                    + "<td>" + prodLineItem.getMonthlyClicks() + "</td>"
                    + "<td>" + prodLineItem.getMonthlySubtotalCredit() + "</td>" + "</tr>";

        }

        emailBody += "</table></body></html>";
//        int count = 0;
//        for (ProductLineItemEntity prodLineItem : monthlyPaymentEntity.getListOfProductLineItems()) {
//            count++;
//            emailBody += count
//                    + "             " + prodLineItem.getProduct().getProductName()
//                    + "             " + prodLineItem.getMonthlyClicks()
//                    + "             " + prodLineItem.getMonthlySubtotalCredit() + "\n";
//        }
        emailBody += "<br/>Total Line Item: " + monthlyPaymentEntity.getListOfProductLineItems().size() + "<br/>Total Amount: " + NumberFormat.getCurrencyInstance().format(monthlyPaymentEntity.getTotalPayable()) + "<br/><br/><br/>";
        emailBody += "Please do make payment at your nearest convenience.<br/><br/><br/>Yours Sincerely, <br/>Moolah Enterprise";

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
                msg.setSubject("Monthly Payment Invoice from Moolah Enterprise");
                msg.setContent(emailBody, "text/html");
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

    public Boolean emailResetPassword(CustomerEntity customer, String fromEmailAddress, String toEmailAddress, Calendar requestedDate, String pathParam) {
        String emailBody = "";

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dateRequested = format.format(requestedDate);
        

        emailBody += "Dear " + customer.getFullName() + "\n\n";
        emailBody += "You have requested to reset your password on " + dateRequested + "\n\n";
        emailBody += "Please click the following link: " + ".../?param=" + pathParam + "\n\n";

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
                msg.setSubject("Reset Password from Moolah Enterprise");
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

    public Boolean emailResetPassword(CompanyEntity company, String fromEmailAddress, String toEmailAddress, Calendar requestedDate, String pathParam) {
        String emailBody = "";

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dateRequested = format.format(requestedDate);

        emailBody += "Dear " + company.getCompanyName() + "\n\n";
        emailBody += "You have requested to reset your password on " + dateRequested + "\n\n";
        emailBody += "Please click the following link: " + ".../?param=" + pathParam + "\n\n";

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
                msg.setSubject("Reset Password from Moolah Enterprise");
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

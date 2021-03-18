/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import ejb.entity.MonthlyPaymentEntity;

import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import util.email.EmailManager;

/**
 *
 * @author nickg
 */
@Stateless
public class EmailSessionBean implements EmailSessionBeanLocal {

    private final String FROM_EMAIL_ADDRESS = "Moolah Enterprise <moolahenterprise@gmail.com>";
    private final String GMAIL_USERNAME = "moolahenterprise@gmail.com";
    private final String GMAIL_PASSWORD = "moolah1234";

    @Override
    public Boolean emailCreditTopupNotificationSync(CompanyEntity company, String toEmailAddress) {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCreditTopupNotification(company, FROM_EMAIL_ADDRESS, toEmailAddress);
        System.out.println("Email Session Bean : Sending email!");
        
        return result;
    }

    @Override
    public Boolean emailReminderAccountDeactivatedSync(CompanyEntity company, String toEmailAddress) {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailReminderAccountDeactivated(company, FROM_EMAIL_ADDRESS, toEmailAddress);
        return result;
    }

    @Asynchronous
    @Override
    public Future<Boolean> emailCreditTopupNotificationAsync(CompanyEntity company, String toEmailAddress) throws InterruptedException {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCreditTopupNotification(company, FROM_EMAIL_ADDRESS, toEmailAddress);

        return new AsyncResult<>(result);
    }

   
    @Override
    public Boolean emailMonthlyPaymentInvoice(MonthlyPaymentEntity monthlyPaymentEntity, String toEmailAddress){
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailMonthlyPaymentInvoice(monthlyPaymentEntity, FROM_EMAIL_ADDRESS, toEmailAddress);
        return result;
    }
}

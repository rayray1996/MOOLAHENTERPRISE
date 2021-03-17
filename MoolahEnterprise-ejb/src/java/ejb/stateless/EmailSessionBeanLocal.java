/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CompanyEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author nickg
 */
@Local
public interface EmailSessionBeanLocal {

    public Future<Boolean> emailCreditTopupNotificationAsync(CompanyEntity company, String toEmailAddress) throws InterruptedException;

    public Boolean emailCreditTopupNotificationSync(CompanyEntity company, String toEmailAddress);

    public Boolean emailReminderAccountDeactivatedSync(CompanyEntity company, String toEmailAddress);

}

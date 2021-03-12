/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

/**
 *
 * @author Ada Wong
 */
@Entity
public class QuestionnaireEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionnaireId;
    
    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.DATE)
    private GregorianCalendar dateCompleted;
   
    @OneToMany
    @Column(nullable = true)
    private List<QuestionEntity> listOfQuestions;

    public QuestionnaireEntity() {
    }

    public QuestionnaireEntity(GregorianCalendar dateCompleted, List<QuestionEntity> listOfQuestions) {
        this.dateCompleted = dateCompleted;
        this.listOfQuestions = listOfQuestions;
    }

    
    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionnaireId != null ? questionnaireId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the questionnaireId fields are not set
        if (!(object instanceof QuestionnaireEntity)) {
            return false;
        }
        QuestionnaireEntity other = (QuestionnaireEntity) object;
        if ((this.questionnaireId == null && other.questionnaireId != null) || (this.questionnaireId != null && !this.questionnaireId.equals(other.questionnaireId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.QuestionnaireEntity[ id=" + questionnaireId + " ]";
    }

    public GregorianCalendar getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(GregorianCalendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public List<QuestionEntity> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<QuestionEntity> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import util.enumeration.CategoryEnum;

/**
 *
 * @author Ada Wong
 */
@Entity
public class QuestionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    @Column(unique = true)
    private String questionDescription; 
    @Enumerated(EnumType.STRING)
    private CategoryEnum categoryEnum;
    
    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn (nullable = false)
    private List<OptionEntity> listOfOptions; 

    public QuestionEntity() {
    }

    public QuestionEntity(String questionDescription, CategoryEnum categoryEnum, List<OptionEntity> listOfOptions) {
        this.questionDescription = questionDescription;
        this.categoryEnum = categoryEnum;
        this.listOfOptions = listOfOptions;
    }
    
    
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionId != null ? questionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the questionId fields are not set
        if (!(object instanceof QuestionEntity)) {
            return false;
        }
        QuestionEntity other = (QuestionEntity) object;
        if ((this.questionId == null && other.questionId != null) || (this.questionId != null && !this.questionId.equals(other.questionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.QuestionEntity[ id=" + questionId + " ]";
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public CategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(CategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public List<OptionEntity> getListOfOptions() {
        return listOfOptions;
    }

    public void setListOfOptions(List<OptionEntity> listOfOptions) {
        this.listOfOptions = listOfOptions;
    }
    
}

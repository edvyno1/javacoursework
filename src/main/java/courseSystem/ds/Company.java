package courseSystem.ds;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Company extends User implements Serializable {

    private String companyName;
    private String personOfContact;

    public Company(String login, String password, String companyName, String personOfContact) {
        super(login, password);
        this.companyName = companyName;
        this.personOfContact = personOfContact;
    }

    public Company() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPersonOfContact() {
        return personOfContact;
    }

    public void setPersonOfContact(String personOfContact) {
        this.personOfContact = personOfContact;
    }
}

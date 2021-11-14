package courseSystem.ds;

public class Company {

    private String companyName;
    private String personOfContact;

    public Company(String companyName, String personOfContact) {
        this.companyName = companyName;
        this.personOfContact = personOfContact;
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

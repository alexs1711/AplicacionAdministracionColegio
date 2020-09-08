package Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.java2d.pipe.SpanShapeRenderer;

public class StudentData {



    private final StringProperty ID;  /*Uso String Property ya que con String no se actualizaría el cambio en inmediato en el Java fx tendria que usar observadores y demas */
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty DOB;
/*constructor para meter a las private final sus respectivos valores y que estas no sean modificadas y que sean observables al instante*/
    public StudentData(String id,String firstname,String lastname ,String email,String dob){

        this.ID = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(firstname);
        this.lastName = new SimpleStringProperty(lastname);
        this.email = new SimpleStringProperty(email);
        this.DOB = new SimpleStringProperty(dob);
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getDOB() {
        return DOB.get();
    }

    public StringProperty DOBProperty() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB.set(DOB);
    }

}

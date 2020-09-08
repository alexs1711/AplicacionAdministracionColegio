package Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class OneRepresentativeData {


    private final StringProperty Division;
    private final StringProperty ID;


    public OneRepresentativeData(String division, String id) {

        this.Division = new SimpleStringProperty(division);
        this.ID = new SimpleStringProperty(id);


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


    public String getDivision () {
        return Division.get();
    }

    public StringProperty divisionProperty () {
        return Division;
    }

    public void setDivision (String division){
        this.Division.set(division);
    }
}

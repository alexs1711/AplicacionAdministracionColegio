package Admin;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class LoginData {
    private final StringProperty Name;
    private final StringProperty Password;
    private final StringProperty Division;
    private final StringProperty ID;
    private ImageView Photo;

    public LoginData(String name, String password, String division, String id, ImageView loginphoto) {

        this.Division = new SimpleStringProperty(division);
        this.Name = new SimpleStringProperty(name);
        this.Password = new SimpleStringProperty(password);
        this.ID = new SimpleStringProperty(id);
        this.Photo = loginphoto;

    }

    public ImageView getPhoto() {
        return Photo;
    }

    public void setPhoto(ImageView photo) {
        Photo = photo;
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

    public String getName () {
            return Name.get();
        }

        public StringProperty nameProperty () {
            return Name;
        }

        public void setName (String name){
            this.Name.set(name);
        }

        public String getPassword () {
            return Password.get();
        }

        public StringProperty passwordProperty () {
            return Password;
        }

        public void setPassword (String password){
            this.Password.set(password);
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


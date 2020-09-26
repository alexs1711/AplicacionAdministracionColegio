package Admin;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import loginapp.option;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.sqlite.SQLiteException;

public class AdminController implements Initializable {
    private dbConnection dc;
    @FXML
    private AnchorPane userpane;
    private ObservableList<String> RowDiv;



    public void initialize(URL url, ResourceBundle rb) {
        RowDiv = FXCollections.observableArrayList();
        RowDiv.add("Alumno");
        RowDiv.add("Profesor");
        RowDiv.add("Admin");
        this.dc = new dbConnection(); /*conexion inicializada*/
        this.division.setItems(FXCollections.observableArrayList(option.values()));
        accesstable.setEditable(true);
        namecolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordcolumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.adduserbutton.setDisable(true);
        this.clearbutton.setDisable(true);
        this.deletebutton.setDisable(true);
        userpane.setPickOnBounds(false);/*asi el movimiento del mouse se detecta aunque el anchorpane tenga nodos encima suya*/
    }

    public void activateButtons() {
        activateAddClearButtons();
        activateDeleteButton();
    }

    public void activateAddClearButtons() {
        String user = this.name.getText();
        String password = this.password.getText();

        boolean activate = (user.trim().isEmpty() || user.isEmpty() || password.trim().isEmpty() || password.isEmpty() || this.division.getValue() == null);
        adduserbutton.setDisable(activate);
        clearbutton.setDisable(activate);
    }

    public void activateDeleteButton() {
        LoginData selectedItem = accesstable.getSelectionModel().getSelectedItem();
        boolean activate = (selectedItem == null);
        deletebutton.setDisable(activate);
    }


    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<option> division;
    @FXML
    private TextField searchfield;
    @FXML
    private TextField searchpassword;

    @FXML
    private TableView<LoginData> accesstable;


    @FXML
    private TableColumn<LoginData, ImageView> loginImage;
    @FXML
    private TableColumn<LoginData, String> namecolumn;
    @FXML
    private TableColumn<LoginData, String> passwordcolumn;
    @FXML
    private TableColumn<LoginData, String> divisioncolumn;
    @FXML
    private TableColumn<LoginData, String> id2column;


    private ObservableList<LoginData> info;
    private String sqlLogin = "SELECT * FROM login";

    @FXML
    private Button clearbutton;
    @FXML
    private Button adduserbutton;
    @FXML
    private Button deletebutton;


    @FXML
    private void loadLoginData(ActionEvent event) throws SQLException {
        try {

            Connection conn = dbConnection.getConnection();
            this.info = FXCollections.observableArrayList();


            ResultSet rs = conn.createStatement().executeQuery(sqlLogin);
            while (rs.next())/*Mientras haya un siguiente en la tabla sql*/ {
                /*Añado aqui las imagenes ya que asi declaro nuevas imageview y me deja que se repita multiples veces, declarandolo fuera no te lo permite
                 * ya que jacafx imageview no deja repetir nodos, las image si pero las imageview no*/
                ImageView teacherPng = new ImageView(new Image(this.getClass().getResourceAsStream("/Admin/teacher.png")));
                ImageView studentPng = new ImageView(new Image(this.getClass().getResourceAsStream("/Admin/student.png")));
                switch (rs.getString(3)) {
                    case "Profesor":
                        this.info.add(new LoginData(rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                teacherPng)); /*nuevo estudiante */
                        break;
                    case "Estudiante":
                        this.info.add(new LoginData(rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                studentPng));
                        break;
                    default:
                        this.info.add(new LoginData(rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                null));
                        break;
                }
            }


        } catch (SQLException e) {
            System.err.println("Error " + e);
        }


        this.namecolumn.setCellValueFactory(new PropertyValueFactory<LoginData, String>("Name"));
        this.passwordcolumn.setCellValueFactory(new PropertyValueFactory<LoginData, String>("Password"));
        this.divisioncolumn.setCellValueFactory(new PropertyValueFactory<LoginData, String>("Division"));
        this.divisioncolumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(),RowDiv));
        this.divisioncolumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LoginData, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LoginData, String> event) {
               /*En resumen este metodo recibe los cambios de la columna especifica y procede a detectar
                este nuevo valor indicado con la combobox y lo sube a la base sql*/
                String selectedItem = event.getNewValue();
                LoginData selectedRowItem = accesstable.getSelectionModel().getSelectedItem();
                try {
                    String sqlDeleteLogin = "UPDATE login SET division =  ? WHERE ID = ?";
                    Connection conn = dbConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sqlDeleteLogin);
                    stmt.setString(1, selectedItem);
                    stmt.setString(2, selectedRowItem.getID());
                    stmt.execute();
                    conn.close();
                }
                catch (SQLException e){

                }
            }
        });
        this.id2column.setCellValueFactory(new PropertyValueFactory<LoginData, String>("ID"));
        this.loginImage.setPrefWidth(50);
        this.loginImage.setMaxWidth(50);
        this.loginImage.setCellValueFactory(new PropertyValueFactory<LoginData, ImageView>("Photo"));
        this.accesstable.setItems(this.info);
        this.accesstable.setEditable(true);
    }

    @FXML
    private AnchorPane ap;


    @FXML
    private void addLogin(ActionEvent event) {
        String sqlInsert = "INSERT INTO login(username,password,division) VALUES (?,?,?)";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);


            if (this.division.getSelectionModel().isEmpty()) {
                /*Cargo la stage ya iniciada de adminStage */
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.hide();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error, selecciona estudiante o profesor");
                alert.setHeaderText("Error");
                alert.setContentText("Tienes que seleccionar una de las dos opciones");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    stage.show();
                }
            } else if (!this.name.getText().matches("^[a-zA-Z]+$")) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.hide();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error, el nombre no puede contener caracteres no comprendidos");
                alert.setHeaderText("Error");
                alert.setContentText("El nombre de usuario solo puede contener caracteres a-z o A-Z sin espacios, cambie el nombre de " + this.name.getText());
                alert.showAndWait();
                stage.show();
            } else {

                stmt.setString(3, ((option) this.division.getValue()).toString());
                stmt.setString(1, this.name.getText());  /*lo metemos en el place holder 1 con la info del FXML textfield de id*/
                stmt.setString(2, this.password.getText());
                stmt.execute();
            }
            conn.close();
        } /*catch (SQLiteException e){
            if(e.Number){
                System.out.println("Mal introducido");
            }
        }*/ catch (SQLException throwables) {

            if (throwables.getErrorCode() == 19) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.hide();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error, ese nombre ya existia");
                alert.setHeaderText("Error");
                alert.setContentText("Ese nombre ya existia!!!");
                alert.showAndWait();
                stage.show();
            }
            ;

        }
    }

    @FXML
    private void deleteRowLogin(ActionEvent event) throws SQLException {
        LoginData deleteItem = accesstable.getSelectionModel().getSelectedItem();
        accesstable.getItems().removeAll(deleteItem);

        try {
            String sqlDeleteLogin = "DELETE FROM login WHERE username =  ?";

            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDeleteLogin);
            stmt.setString(1, deleteItem.getName());
            stmt.execute();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error " + e);
        } catch (NullPointerException ex) {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error, no has seleccionado a nadie");
            alert.setHeaderText("Error");
            alert.setContentText("Selecciona a alguien!!!");
            alert.showAndWait();
            stage.show();
        }
    }

    @FXML
    private void clearFieldsLogin(ActionEvent event) {
        this.name.setText("");
        this.password.setText("");
        this.division.setValue(null);
    }


    public void ModifySelectedRowLoginName(TableColumn.CellEditEvent<LoginData, String> loginDataStringCellEditEvent) throws SQLException {
        LoginData selectedItem = accesstable.getSelectionModel().getSelectedItem();
        selectedItem.setName(loginDataStringCellEditEvent.getNewValue());
        try {
            if (!selectedItem.getName().matches("^[a-zA-Z]+$")) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.hide();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error, el nombre no puede contener caracteres no comprendidos");
                alert.setHeaderText("Error");
                alert.setContentText("El nombre de usuario solo puede contener caracteres a-z o A-Z sin espacios, cambie el nombre de " + selectedItem.getName());
                alert.showAndWait();
                stage.show();
            } else {
                String sqlDeleteLogin = "UPDATE login SET username =  ? WHERE ID = ?";
                Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlDeleteLogin);
                stmt.setString(1, selectedItem.getName());
                stmt.setString(2, selectedItem.getID());
                stmt.execute();
                conn.close();
            }
        } catch (SQLiteException e) {
            if (e.getErrorCode() == 19) {
                Stage stage = (Stage) ap.getScene().getWindow();
                stage.hide();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No puedes poner un nombre ya existente");
                alert.setHeaderText("Error");
                alert.setContentText("Ese nombre ya existia, no se subira el cambio a la BBDD!!!");
                alert.showAndWait();
                stage.show();

            }
        }
    }

    public void ModifySelectedRowLoginPassword(TableColumn.CellEditEvent<LoginData, String> loginDataStringCellEditEvent) throws SQLException {
        LoginData selectedItem = accesstable.getSelectionModel().getSelectedItem();
        selectedItem.setPassword(loginDataStringCellEditEvent.getNewValue());
        try {

            String sqlDeleteLogin = "UPDATE login SET password =  ? WHERE ID = ?";
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDeleteLogin);
            stmt.setString(1, selectedItem.getPassword());
            stmt.setString(2, selectedItem.getID());
            stmt.execute();
            conn.close();
        } catch (SQLiteException e) {

            Stage stage = (Stage) ap.getScene().getWindow();
            stage.hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());
            alert.showAndWait();
            stage.show();

        }
    }


    public void SearchUserName() {
        try {
            if (searchpassword.getText().equals("")) {
                FilteredList<LoginData> filteredData = new FilteredList<>(this.info, p -> true);
                searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(user -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (user.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        } /* else if (user.getPassword().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                      return true;
                  } else if (String.valueOf(user.getID()).indexOf(lowerCaseFilter) != -1)
                      return true;*/ else
                            return false;
                    });
                });

                SortedList<LoginData> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(accesstable.comparatorProperty());
                accesstable.setItems(sortedData);

            } else {
                FilteredList<LoginData> filteredData = new FilteredList<>(this.info, p -> true);
                searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(user -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (user.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 && user.getPassword().toLowerCase().indexOf(searchpassword.getText()) != -1) {
                            return true;
                        } else
                            return false;
                    });
                });

                SortedList<LoginData> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(accesstable.comparatorProperty());
                accesstable.setItems(sortedData);

            }

        } catch (NullPointerException ex) {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error, no has seleccionado a nadie");
            alert.setHeaderText("Error");
            alert.setContentText("Selecciona a alguien!!!");
            alert.showAndWait();
            stage.show();
        }
    }

    public void SearchPassword() {
        try {
            if (searchfield.getText().equals("")) {
                FilteredList<LoginData> filteredData = new FilteredList<>(this.info, b -> true);
                searchpassword.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(user -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (user.getPassword().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        } else
                            return false;
                    });
                });

                SortedList<LoginData> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(accesstable.comparatorProperty());
                accesstable.setItems(sortedData);
            } else {

                FilteredList<LoginData> filteredData = new FilteredList<>(this.info, p -> true);
                searchpassword.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(user -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (user.getPassword().toLowerCase().indexOf(lowerCaseFilter) != -1 && user.getName().toLowerCase().indexOf(searchfield.getText()) != -1) {
                            return true;
                        } else
                            return false;
                    });
                });

                SortedList<LoginData> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(accesstable.comparatorProperty());
                accesstable.setItems(sortedData);

            }
        } catch (NullPointerException ex) {
            Stage stage = (Stage) ap.getScene().getWindow();
            stage.hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error, no has seleccionado a nadie");
            alert.setHeaderText("Error");
            alert.setContentText("Selecciona a alguien!!!");
            alert.showAndWait();
            stage.show();
        }
    }

    public void clearSearchButton() {
        this.searchfield.setText("");
    }

    public void clearPasswordButton() {
        this.searchpassword.setText("");
    }

}

/*
ObservableSet<OneRepresentativeData> OneRepresentative;

    @FXML
    private void getOneRepresentativeOfEach(ActionEvent event){


        try {

            Connection conn = dbConnection.getConnection();
            this.OneRepresentative = FXCollections.observableSet();


            ResultSet rs = conn.createStatement().executeQuery(sqlLogin);
            while (rs.next())/*Mientras haya un siguiente en la tabla sql {*/
        /*Añado aqui las imagenes ya que asi declaro nuevas imageview y me deja que se repita multiples veces, declarandolo fuera no te lo permite
         * ya que jacafx imageview no deja repetir nodos, las image si pero las imageview no*/
/*
                ImageView teacherPng = new ImageView(new Image(this.getClass().getResourceAsStream("/Admin/teacher.png")));
                ImageView studentPng = new ImageView(new Image(this.getClass().getResourceAsStream("/Admin/student.png")));
                switch(rs.getString(3)) {
                    case "Profesor":
                        this.OneRepresentative.add(new OneRepresentativeData(rs.getString(3),rs.getString(4))); /*nuevo estudiante*/
                      /*  break;
                    case "Estudiante":
                        this.OneRepresentative.add(new OneRepresentativeData(rs.getString(3),rs.getString(4)));
                        break;

                }
            }


        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        System.out.println(this.OneRepresentative);
        
    }

    }
    @FXML
    private void UploadTableChangesLogin(ActionEvent event) throws SQLException {
        LoginData loginData = accesstable.getSelectionModel().getSelectedItem();
        try {
            String sqlDeleteLogin = "UPDATE login SET username =  ?, password = ? WHERE username = ?";
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDeleteLogin);
            stmt.setString(1, loginData.getName());
            stmt.setString(2, loginData.getPassword());
            stmt.execute();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error " + e);
        }*/


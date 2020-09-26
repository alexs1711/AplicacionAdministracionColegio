/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginapp;

import Admin.AdminController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import students.StudentsController;
import teachers.TeachersController;

import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URL;

/**
 *
 * @author l3x
 */
public class LoginController implements Initializable {

    LoginModel loginModel = new LoginModel();
    /*Label  */
    @FXML
    private Label dbstatus;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<option> comboBox;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginStatus;

    public void initialize(URL url, ResourceBundle rb){
            if(this.loginModel.isDatabaseConnected()){
                this.dbstatus.setText("Conectado a la BBDD");
            }else {
                this.dbstatus.setText("No conectado a la BBDD");
            }

            this.comboBox.setItems(FXCollections.observableArrayList(option.values()));


    }



    public void Login(){
        try {
            if ( (option)this.comboBox.getValue() == null || this.username.getText().isEmpty() || this.password.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de acceso");
                alert.setHeaderText("Error");
                alert.setContentText("Has cometido un error al introducir los datos \n Revisa el nombre de usuario, contraseña y su categoria.");
                alert.showAndWait();
            }

             else if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option)this.comboBox.getValue()).toString())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                    switch(((option)this.comboBox.getValue()).toString()) {
                        case "Profesor":

                            teacherLogin();
                            break;
                        case "Estudiante":
                            studentLogin();
                            break;
                        case "Admin":
                            adminLogin();
                            break;
                    }
            }

            else {
                    this.loginStatus.setText("Error, revisa\n el usuario o \ncontraseña");
            }
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }
    public void studentLogin(){
        try {
            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane)loader.load(getClass().getResource("/students/studentsFXML.fxml").openStream());
            StudentsController studentController = (StudentsController)loader.getController();

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("Menu Estudiantes");
            userStage.setResizable(false);
            userStage.show();

        }   catch (IOException exception){
            exception.printStackTrace();
        }
    }
    public void adminLogin(){
        try{
            Stage adminStage = new Stage();
            FXMLLoader adminLoader = new FXMLLoader();
            Pane adminRoot = (Pane)adminLoader.load(getClass().getResource("/Admin/Admin.fxml").openStream());
            AdminController adminController = (AdminController)adminLoader.getController();

            Scene scene = new Scene(adminRoot);
            adminStage.setScene(scene);
            adminStage.setTitle("Menu Administrador");
            adminStage.setResizable(false);
            adminStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void teacherLogin(){
        try{
            Stage teacherStage = new Stage();
            FXMLLoader teacherLoader = new FXMLLoader();
            Pane teacherroot = (Pane)teacherLoader.load(getClass().getResource("/teachers/Teachers.fxml").openStream());
            TeachersController teachersController = (TeachersController) teacherLoader.getController();

            Scene scene = new Scene(teacherroot);
            teacherStage.setScene(scene);
            teacherStage.setTitle("Menu Profesor");
            teacherStage.setResizable(false);
            teacherStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void EnterKEY(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            Login();
        }
    }
}

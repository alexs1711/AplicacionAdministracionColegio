/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginapp;

import Admin.AdminController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import students.StudentsController;

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
    @FXML
    public void Login(ActionEvent event){
        try {
            if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option)this.comboBox.getValue()).toString())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                    switch(((option)this.comboBox.getValue()).toString()) {
                        case "Profesor":
                            adminLogin();
                            break;
                        case "Estudiante":
                            studentLogin();
                            break;

                    }
            }
            else {
                    this.loginStatus.setText("Error, revisa el usuario/contraseña");
            }
        }
        catch (Exception localException){

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
}

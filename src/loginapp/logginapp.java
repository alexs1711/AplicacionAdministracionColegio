/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class logginapp extends Application {
    
    public void start(Stage stage)throws Exception{
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("Login.fxml"));/*Nodo raiz*/
        
        Scene scene = new Scene (root);/*Se crea la ventana*/
        stage.setScene(scene);/*Se crea el contenido dentro de la ventana es decir scene, se puede tener solo 1 scene a la vez */
        stage.setTitle("BBDD Estudiantes");
        stage.show();
    
    }
    public static void main(String[] args) {
        launch(args);/*Simplemente llama el metodo estatico launcha para iniciar la aplicacion*/
    }

    
}

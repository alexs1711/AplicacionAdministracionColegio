package students;

import Admin.StudentData;
import com.sun.javafx.collections.VetoableListDecorator;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class StudentsController {
    @FXML
    private TableView<StudentData> studenttable;

    @FXML
    private TableColumn<StudentData, String> idcolumn;

    @FXML
    private TableColumn<StudentData, String> firstnamecolumn;

    @FXML
    private TableColumn<StudentData, String> lastnamecolumn;

    @FXML
    private TableColumn<StudentData, String> emailcolumn;

    @FXML
    private TableColumn<StudentData, String> dobcolumn;

    private dbConnection dc;
    private ObservableSet<StudentData> data;

    private String sql = "SELECT * FROM students";

    public void initialize(URL url, ResourceBundle rb) throws SQLException {
        this.dc = new dbConnection(); /*conexion inicializada*/

    }
    @FXML
    private void loadStudentData(ActionEvent event) throws SQLException {
        Set<StudentData> set = new HashSet<StudentData>();
        StudentsController.CustomObservableList<StudentData> list = null;
        try {
            Connection conn = dbConnection.getConnection();

            list = new StudentsController.CustomObservableList<StudentData>(FXCollections.observableArrayList(set));


            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next())/*Mientras haya un siguiente en la tabla sql*/ {
                list.add(new StudentData(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5))); /*nuevo estudiante */
            }


        } catch (SQLException e) {
            System.err.println("Error " + e);
        }

        this.idcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("ID")); /*una vez
        tenemos los datos ya dentro de la clase StudenData lo metemos en el FXML */
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("firstName"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("lastName"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("email"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("DOB"));

        this.studenttable.setItems(null);
        this.studenttable.setItems(list);
    }
    public class CustomObservableList<E> extends VetoableListDecorator<E> {

        public CustomObservableList(ObservableList<E> decorated) {
            super(decorated);
        }

        @Override
        protected void onProposedChange(List<E> toBeAdded, int... indexes) {
            for (E e : toBeAdded) {
                if (contains(e)) {
                    throw new IllegalArgumentException("Duplicado añadido");
                }
            }
        }
    }
}

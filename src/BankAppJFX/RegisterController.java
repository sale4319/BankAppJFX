/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import BankAppJFX.DbService;
import BankAppJFX.DbService;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Aco PC
 */
public class RegisterController implements Initializable {

    public DbService dbservice = new DbService();
    private double x = 0, y = 0;
    private Stage stage;

    @FXML
    private TextField tf_firstName;
    @FXML
    private TextField tf_lastName;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField pf_password;
    @FXML
    private VBox SignUpMainBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        makeDragable();

    }

    private void makeDragable() {
        SignUpMainBox.setOnMousePressed(((event)
                -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));
        SignUpMainBox.setOnMouseDragged(((event)
                -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }));
    }

    @FXML
    public void signOutAction(MouseEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Login.fxml").openStream());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signUpAction(ActionEvent event) throws IOException {
        signUpReusable();

    }

    @FXML
    private void signUpOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signUpReusable();
        }
    }

    private void signUpReusable() {

        StringBuilder warnings = new StringBuilder();
        String username = "", email = "", password = "", firstName = "", lastName = "";
        Connection connection = DbService.getConnection();

        try {
            //Takes and checks input
            if (tf_firstName.getText().isEmpty()) {
                warnings.append("First name must not be empty.\n");
            } else {
                firstName = tf_firstName.getText();
            }
            if (tf_lastName.getText().isEmpty()) {
                warnings.append("Last name must not be empty.\n");
            } else {
                lastName = tf_lastName.getText();
            }

            if (tf_username.getText().isEmpty()) {
                warnings.append("Username must not be empty.\n");
            } else {
                username = tf_username.getText();
            }
            if (tf_email.getText().isEmpty()) {
                warnings.append("Email must not be empty.\n");
            } else {
                email = tf_email.getText();
            }
            if (pf_password.getText().isEmpty()) {
                warnings.append("Password must not be empty.");
            } else {
                password = pf_password.getText();
            }
            if (warnings.length() > 0) {
                //Displays warnings if something is wrong
                Alert alert = new Alert(Alert.AlertType.WARNING, warnings.toString(), ButtonType.OK);
                alert.showAndWait();
            } else {
                //Checks if username and email are already taken
                if (dbservice.chekcIfTaken(tf_username.getText(), tf_email.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Username or email already taken.", ButtonType.OK);
                    alert.showAndWait();

                } else {
                    //Registers new user
                    Statement statement = connection.createStatement();
                    int status = statement.executeUpdate("INSERT INTO users (firstName, lastName, username, email, password)"
                            + " VALUES ('" + firstName + "','" + lastName + "','" + username + "','" + email + "','" + password + "')");
                    if (status > 0) {
                        //Clears text fields after input
                        tf_firstName.clear();
                        tf_lastName.clear();
                        tf_username.clear();
                        tf_email.clear();
                        pf_password.clear();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User registered successfully", ButtonType.OK);
                        alert.showAndWait();

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

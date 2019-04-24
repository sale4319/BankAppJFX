/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Aco PC
 */
public class LoginController implements Initializable {

    public DbService dbservice = new DbService();
    private double x = 0, y = 0;
    private Stage stage;

    @FXML
    private Label isConnected;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField pf_password;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane LoginMainPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDragable();
    }

    public void makeDragable() {
        LoginMainPane.setOnMousePressed(((event)
                -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));
        LoginMainPane.setOnMouseDragged(((event)
                -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }));
    }

    @FXML
    public void closeButtonAction(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    public void loginAction(ActionEvent evt) {
        
            try {
                if (dbservice.checkLogin(tf_username.getText(), pf_password.getText())) {
                    //Hide stage when entering next stage
                    ((Node) evt.getSource()).getScene().getWindow().hide();
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    Pane root = loader.load(getClass().getResource("User.fxml").openStream());
                    UserController userController = (UserController) loader.getController();
                    userController.getUser(tf_username.getText());
                    Scene scene = new Scene(root);
                    //Make Borderless
                    scene.setFill(Color.TRANSPARENT);
                    primaryStage.setScene(scene);
                    primaryStage.initStyle(StageStyle.TRANSPARENT);
                    primaryStage.show();

                } else {
                    isConnected.setText("Username or password is not correct.");
                }
            } catch (SQLException e) {
                isConnected.setText("Username or password is not correct.");
            } catch (IOException e) {
                e.printStackTrace();
            }    
    }

    @FXML
    private void signUpAction(MouseEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("Register.fxml").openStream());

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
    private void loginWithEnter(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            try {
                if (dbservice.checkLogin(tf_username.getText(), pf_password.getText())) {

                    //Hide stage
                    ((Node) evt.getSource()).getScene().getWindow().hide();
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    Pane root = loader.load(getClass().getResource("User.fxml").openStream());
                    UserController userController = (UserController) loader.getController();
                    userController.getUser(tf_username.getText());

                    Scene scene = new Scene(root);

                    scene.setFill(Color.TRANSPARENT);
                    primaryStage.setScene(scene);
                    primaryStage.initStyle(StageStyle.TRANSPARENT);

                    primaryStage.show();

                } else {
                    isConnected.setText("Username or password is not correct.");
                }
            } catch (SQLException e) {
                isConnected.setText("Username or password is not correct.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

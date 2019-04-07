/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Aco PC
 */
public class UserController implements Initializable {

    public Connection connection;
    public DbService dbservice = new DbService();
    private double x = 0, y = 0;
    private Stage stage;
    String user;
    ObservableList<TableModel> oblist = FXCollections.observableArrayList();

    @FXML
    private Label userLbl;
    @FXML
    private Label userLbl1;
    @FXML
    private Button closeButton;
    @FXML
    private Button minButton;
    @FXML
    private Button addCCButton;
    @FXML
    private Button deleteAccButton;
    @FXML
    private Button depositButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private BorderPane UserMainPane;
    @FXML
    private TableView<TableModel> table;
    @FXML
    private TableColumn<TableModel, String> col_id;
    @FXML
    private TableColumn<TableModel, String> col_cardNr;
    @FXML
    private TableColumn<TableModel, String> col_cardType;
    @FXML
    private TableColumn<TableModel, String> col_balance;
    @FXML
    private TextField tf_cardnumber;
    @FXML
    private ComboBox<?> cb_cardtype;
    @FXML
    private TextField tf_deposit;
    @FXML
    private TextField tf_withdraw;
    @FXML
    private Label controlLable;
    @FXML
    private TextField tf_initialDeposit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDragable();

    }

    private void makeDragable() {
        UserMainPane.setOnMousePressed(((event)
                -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));
        UserMainPane.setOnMouseDragged(((event)
                -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }));
    }

    public void GetUser(String user1) {
        this.user = user1;
        userLbl.setText(user1);
        selectUserName(user1);
        populateTable(user1);
    }

    public void selectUserName(String s) {
        //Select and display name in lable
        String sql = "select firstName, lastName from users where username = ?";
        try {
            connection = DbService.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            //loops through result set without specific condition
            while (rs.next()) {
                userLbl1.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateTable(String s) {
        try {
            String sql = "select * from accounts where userId = ?";
            connection = DbService.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                oblist.add(new TableModel(rs.getString("id"), rs.getString("cardNr"),
                        rs.getString("cardType"), rs.getString("Balance")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_cardNr.setCellValueFactory(new PropertyValueFactory<>("cardNr"));
        col_cardType.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        col_balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        table.setItems(oblist);
    }

    @FXML
    public void SignOut(ActionEvent event) {
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
    private void min(ActionEvent event) {
        ((Stage) minButton.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void max(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("Press ESC to exit full-screen mode");

    }

    @FXML
    public void close(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    @FXML
    private void addCCAction(ActionEvent event) {
        StringBuilder warnings = new StringBuilder();
        String cardNr = "", cardType = "";
        connection = DbService.getConnection();
        try {
            if (tf_cardnumber.getText().isEmpty()) {
                warnings.append("Card number field must not be empty.\n");
            } else {
                cardNr = tf_cardnumber.getText();
            }
            if (warnings.length() > 0) {
                controlLable.setText(warnings.toString());
            } else {
                if (dbservice.temp(tf_cardnumber.getText())) {
                    controlLable.setText("Credit card already connected.");
                } else {
                    Statement statement = connection.createStatement();
                    int status = statement.executeUpdate("insert into accounts (userId, cardNr) values ('" + user + "', '" + cardNr + "')");
                    if (status > 0) {

                        tf_cardnumber.clear();

                        controlLable.setText("User registered successfully");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(user);
    }

}

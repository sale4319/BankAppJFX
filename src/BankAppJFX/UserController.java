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
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    public Bank bank = new Bank();
    public Connection connection;
    public DbService dbservice = new DbService();
    private double x = 0, y = 0;
    private Stage stage;
    String user;
    ObservableList<TableModel> oblist = FXCollections.observableArrayList();
    ObservableList<String> boxItems = FXCollections.observableArrayList("MasterCard", "Visa");

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
    private ComboBox cb_cardtype;
    @FXML
    private TextField tf_deposit;
    @FXML
    private TextField tf_withdraw;
    @FXML
    private TextField tf_initialDeposit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDragable();
        cb_cardtype.setValue("Card Type");
        cb_cardtype.setItems(boxItems);

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
        //Select and display name in label
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
        oblist.clear();
        try {
            String sql = "select * from accounts where userId = ?";
            connection = DbService.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                oblist.add(new TableModel(rs.getInt("id"), rs.getString("cardNr"),
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
        int cardNr = 0;
        double amount = 0;
        connection = DbService.getConnection();
        try {
            if (tf_initialDeposit.getText().isEmpty()) {
                warnings.append("Initial deposit must be entered.\n");
            } else {
                try {
                    amount = bank.round(Double.parseDouble(tf_initialDeposit.getText()), 2);
                } catch (NumberFormatException ex) {
                    warnings.append("Initial deposit must be a number.\n");
                }
            }
            if (tf_cardnumber.getText().isEmpty()) {
                warnings.append("Card number field must not be empty.\n");
            } else {
                cardNr = Integer.parseInt(tf_cardnumber.getText());
            }

            if (dbservice.checkCardNr(Integer.parseInt(tf_cardnumber.getText()))) {
                warnings.append("Credit card already connected.\n");

            } else {
                CardType cardType = CardType.Undefined;
                if (cb_cardtype.getValue().equals("MasterCard")) {
                    if (amount >= 50) {
                        cardType = CardType.MasterCard;
                    } else {
                        warnings.append("Initial deposit must be at least $50 for MasterCard.\n");
                    }
                }
                if (cb_cardtype.getValue().equals("Visa")) {
                    if (amount >= 10) {
                        cardType = CardType.Visa;
                    } else {
                        warnings.append("Initial deposit must be at least $10 for Visa.\n");
                    }
                }
                if (cb_cardtype.getValue().equals("Card Type")) {
                    warnings.append("You need to select card type.\n");
                }
                if (warnings.length() > 0) {
                    Alert alert = new Alert(AlertType.WARNING, warnings.toString(), ButtonType.OK);
                    alert.showAndWait();
                }
                if (cardType != CardType.Undefined) {

                    int accountId = bank.openAccount(user, cardNr, cardType, amount);
                    populateTable(user);
                    tf_initialDeposit.clear();
                    tf_cardnumber.clear();
                    cb_cardtype.setValue("Card Type");
                    Alert alert = new Alert(AlertType.CONFIRMATION, "Card registered successfully", ButtonType.OK);
                    alert.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Card number  must be a number.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteCard() {
        //Selects id in clicked row
        int selectedRow = table.getSelectionModel().getSelectedItem().getId();
        dbservice.deleteAccount(selectedRow);
        table.getItems().removeAll(table.getSelectionModel().getSelectedItem());

    }

}

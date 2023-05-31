package com.example.projetjavafx;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.*;
import java.util.ArrayList;

public class HelloController {
    @FXML
    private TextField monID;
    @FXML
    private PasswordField monMdp;

    @FXML
    private TextField numCle;

    @FXML
    private TextField coulCle;

    @FXML
    private TextField ouvertureCle;

    public String URL = "jdbc:mysql://172.19.0.27:3306/tpcle";

    public String LOGIN = "phpmyadmin";

    public String PASSWORD = "0550002D";

    private ComboBox<Cle> LISTE ;

    final ObservableList<Cle> listCle = FXCollections.observableArrayList();



     public  void main(String[] args)throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
         try {
             ArrayList<Cle> laliste = new ArrayList<Cle>();

             Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);

             Statement stmt = connexion.createStatement();
             String req = "SELECT * FROM cle";
             ResultSet res1 = stmt.executeQuery(req);
             while(res1.next()){
                String numCle = res1.getString("numero");

                String coleurCle = res1.getString("couleur");
                String ouvertureCle = res1.getString("ouverture");
                Cle cle1= new Cle(Integer.parseInt(numCle),coleurCle, ouvertureCle);
                 listCle.add(cle1);
                 LISTE.setItems(listCle);

             }
         }
         catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }
    @FXML
    protected void onHelloButtonClick() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {




        try {

            Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);

            Statement stmt = connexion.createStatement();
            String req = "SELECT login, password, droit FROM users";
            ResultSet res1 = stmt.executeQuery(req);
            res1.next();

            int droit = res1.getInt("droit");
            String login = res1.getString("login");
            String mdp = res1.getString("password");

            Hasher hasher = Hashing.sha256().newHasher();
            hasher.putString("test", Charsets.UTF_8);
            HashCode sha256 = hasher.hash();

            System.out.println(mdp);

            Hasher hasherMdp = Hashing.sha256().newHasher();
            hasherMdp.putString(monMdp.getText(), Charsets.UTF_8);
            HashCode monMdpHash = hasherMdp.hash();

            if (login.equals(login) && monMdpHash.equals(sha256)) {
                Stage newWindow = new Stage();
                FXMLLoader fxmlLoader = new
                        FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 640, 480);
                newWindow.setScene(scene);
                // Specifies the modality for new window.
                newWindow.initModality(Modality.APPLICATION_MODAL);
                newWindow.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText("Connexion");
                alert.setContentText("identifiant erroné");
                alert.showAndWait();
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSearchButtonClick(ActionEvent actionEvent) {

    }

    public void onModifButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    protected void onAddButtonClick()throws NoSuchAlgorithmException, NoSuchProviderException, IOException  {
        try {
            Cle cle = new Cle(Integer.parseInt(numCle.getText()),coulCle.getText(),ouvertureCle.getText());
            Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);

            Statement stmt = connexion.createStatement();
            String req = "INSERT INTO cle (numero, couleur, ouverture) VALUES (" + cle.getNumero() + ", '" + cle.getCouleur() + "', '" + cle.getOuverture() + "')";
            stmt.executeUpdate(req);


        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSuppButtonClick(ActionEvent actionEvent) {
    }
}

package todoapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EntryPageController extends ToDoApp implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameBox;

    @FXML
    private TextField passwordBox;

    @FXML
    private Text wrongPassBox;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginButton.setOnAction((actionEvent) -> {
            try {
                Parent homePageParent = FXMLLoader.load(getClass().getResource("FXMLHomePage.fxml"));
                Scene homePageScene = new Scene(homePageParent);
                Stage appStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();

                if(isValidCredentials()){
                    appStage.hide();
                    appStage.setScene(homePageScene);
                    appStage.show();
                }
                else{
                    usernameBox.clear();
                    passwordBox.clear();
                    wrongPassBox.setVisible(true);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


    }

    private boolean isValidCredentials(){
        boolean letIn=false;
        System.out.println("SELECT * FROM Users WHERE username = " + usernameBox.getText() + "'" + "AND password" + "'" + passwordBox.getText());

        Connection c = null;
        java.sql.Statement statement = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:todoapp.db");
            c.setAutoCommit(false);

            System.out.println("Opened Database Successfully");
            statement = c.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE username= " +"'"+
                    usernameBox.getText()+ "'" + "AND password=" + "'" + passwordBox.getText() + "'");
            while (rs.next()){
                if(rs.getString("username") != null && rs.getString("password") != null ){
                    String userName= rs.getString("username");
                    System.out.println("username=" + userName);
                    String password = rs.getString("password");
                    System.out.println("password=" + password);
                    letIn = true;
                }
            }
            rs.close();
            statement.close();
            c.close();
        }

        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage() );
            System.exit(0);
        }

        System.out.print("Operation done succesfully");
        return letIn;

    }



}

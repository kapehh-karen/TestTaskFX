package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private RndModel model = new RndModel();

    private ListView listView;
    private TextField textField;
    private Button btnAdd;
    private Button btnRemove;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        listView = (ListView) root.lookup("#list_of_nums");
        textField = (TextField) root.lookup("#txt_n");
        btnAdd = (Button) root.lookup("#btn_add");
        btnRemove = (Button) root.lookup("#btn_remove");

        btnAdd.setOnAction(event -> {
            model.addItems(Integer.valueOf(textField.getText()));
            listView.setItems(FXCollections.observableArrayList(model.listOfItems()));
        });

        btnRemove.setOnAction(event -> {
            model.removeItems(Integer.valueOf(textField.getText()));
            listView.setItems(FXCollections.observableArrayList(model.listOfItems()));
        });

        primaryStage.setTitle("Задание \"ЛАНИТ\"");
        primaryStage.setScene(new Scene(root, 300, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

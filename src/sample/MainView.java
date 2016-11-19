package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainView extends Application {
    private Controller controller = new Controller();

    private Thread threadTimeUpdate;

    private ListView listView;
    private TextField textField;
    private Button btnAdd;
    private Button btnRemove;
    private Label labelTime;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        listView = (ListView) root.lookup("#list_of_nums");
        textField = (TextField) root.lookup("#txt_n");
        btnAdd = (Button) root.lookup("#btn_add");
        btnRemove = (Button) root.lookup("#btn_remove");
        labelTime = (Label) root.lookup("#lbl_time");

        btnAdd.setOnAction(event -> {
            try {
                controller.addItems(Integer.valueOf(textField.getText()));
            } catch (NumberFormatException e) {
                // NumberFormatException
            }
            listView.setItems(FXCollections.observableArrayList(controller.getListOfItems()));
        });

        btnRemove.setOnAction(event -> {
            try {
                controller.removeItems(Integer.valueOf(textField.getText()));
            } catch (NumberFormatException e) {
                // NumberFormatException
            }
            listView.setItems(FXCollections.observableArrayList(controller.getListOfItems()));
        });

        primaryStage.setTitle("Задание \"ЛАНИТ\"");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();

        threadTimeUpdate = new Thread(() -> {
            try {
                SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
                while (true) {
                    Thread.sleep(1000);
                    Platform.runLater(() -> labelTime.setText(sdfDate.format(new Date())));
                }
            } catch (InterruptedException e) {
                // Exit application
            }
        });
        threadTimeUpdate.start();
    }

    @Override
    public void stop() throws Exception {
        threadTimeUpdate.interrupt();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

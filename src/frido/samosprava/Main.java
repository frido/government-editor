package frido.samosprava;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

		    ChoiceBox<String> collectionChoiceBox = new ChoiceBox<>();
		    ChoiceBox<String> collectionChoiceBox2 = new ChoiceBox<>();
		    VBox left = new VBox(collectionChoiceBox, collectionChoiceBox2);
		    BorderPane root = new BorderPane();
		    Button btnOpen = new Button("Open");
		    Label chosenFileLabel = new Label();
		    root.setLeft(left);
            btnOpen.setOnAction((e) -> {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    String fileAsString = file.toString();
                    FileDatabase db = new FileDatabase(fileAsString);
                    db.readDB();
                    chosenFileLabel.setText(fileAsString);
                    collectionChoiceBox.getItems().clear();
                    collectionChoiceBox.getItems().addAll(db.getFiles());
                    collectionChoiceBox2.getItems().clear();
                    collectionChoiceBox2.getItems().addAll(db.getCollections());

                } else {
                    chosenFileLabel.setText(null);
                }

            });

            HBox header = new HBox(btnOpen, chosenFileLabel);

            root.setTop(header);

			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("My First Java FX App");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

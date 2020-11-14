package app;

/**
 * Jason Chou and Ty Goldin
 * Group#44
 *
 */
import object.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class userViewSearchResultController {

	public static Account currentAccount;
	public static Album currentAlbum;
	static ObservableList<Photo> photoList = FXCollections.observableArrayList();
	
    @FXML
    private Button backButton;

    @FXML
    private Text albumStatus;

    @FXML
    private Button logoutButton;

    @FXML
    private ListView<Photo> albumPhotos;

    @FXML
    private Button createAlbumButton;
    
    public void initialize() {
    	albumStatus.setText("Search Results");
    	photoList.clear();
    	for (int i = 0; i < currentAlbum.albumPhotos.size(); i++) {
    		photoList.add(currentAlbum.albumPhotos.get(i));
    	}
    	albumPhotos.setItems(photoList);
    	setList();
    }
    
    void setList() {
    	albumPhotos.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                	imageView.setImage(new Image(photo.imagePath));
                	imageView.setFitHeight(64);
                	imageView.setFitWidth(64);
                    setText(photo.caption + " TAGS: " + photo.tags.toString());
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    void backClick(ActionEvent event) throws IOException {
    	Parent userPage = FXMLLoader.load(Photos.class.getResource("/view/userPage.fxml"));
		Scene scene = new Scene(userPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }

    @FXML
    void createAlbumClick(ActionEvent event) throws FileNotFoundException, IOException {
    	TextInputDialog dialog = new TextInputDialog("");
   	 
    	dialog.setTitle("New Album");
    	dialog.setHeaderText("Enter name for Album");
    	 
    	Optional<String> result = dialog.showAndWait();
    	
    	if(!result.isPresent())
    		return;
    	
    	currentAlbum.albumName = result.get();
    	
    	save();
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Success");
    	alert.setContentText(currentAlbum.albumName + " successfully created");
    	alert.showAndWait();
    	
    	Parent userPage = FXMLLoader.load(Photos.class.getResource("/view/userPage.fxml"));
		Scene scene = new Scene(userPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    	
    	
    }

   
    @FXML
    void listViewClick(MouseEvent event) {

    }
    

    @FXML
    void logoutClick(ActionEvent event) throws IOException {
    	Parent loginPage = FXMLLoader.load(Photos.class.getResource("/view/loginPage.fxml"));
		Scene scene = new Scene(loginPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }
    
    public static void writeToFile(Album a) throws FileNotFoundException, IOException {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users/" + currentAccount.username + "/" + a.albumName + ".bin"))) {
			oos.writeObject(a);
		}
		
    }
    
    void save() throws FileNotFoundException, IOException {
    	currentAlbum.albumPhotos.clear();
    	for (int i = 0; i < photoList.size(); i++) {
    		currentAlbum.albumPhotos.add(photoList.get(i));
    	}
    	photoList.clear();
    	
    	
    	writeToFile(currentAlbum);
    }

}

package app;

/**
 * Jason Chou and Ty Goldin
 * Group#44
 *
 */

import object.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class slideShowPageController {
	
	public static Account currentUser;
	public static Photo currentPhoto;
	public static int selectedIndex;
	
	@FXML
	private Text albumStatus;

    @FXML
    private ImageView selectedImage;

    @FXML
    private TextField caption;

    @FXML
    private TextField captureDate;

    @FXML
    private ListView<String> tagList;
    
    @FXML
    private Button previousPhotoButton;

    @FXML
    private Button nextPhotoButton;

    @FXML
    private Button logoutButton;


    public void initialize() {
    	albumStatus.setText("Slide Show: " + userViewPageController.currentAlbum.albumName);
    	try {
    		selectedIndex = 0;
    		setPhoto(userViewPageController.currentAlbum.albumPhotos.get(0));
    	} catch (Exception exc) {
    		
    	}
    }
    
    @FXML
    void nextPhotoClick(ActionEvent event) {
    	selectedIndex++;
    	if (selectedIndex >= userViewPageController.currentAlbum.albumPhotos.size()) {
    		selectedIndex--;
    	} else {
    		setPhoto(userViewPageController.currentAlbum.albumPhotos.get(selectedIndex));
    	}
    }
    
    @FXML
    void previousPhotoClick(ActionEvent event) {
    	selectedIndex--;
    	if (selectedIndex < 0) {
    		selectedIndex++;
    	} else {
    		setPhoto(userViewPageController.currentAlbum.albumPhotos.get(selectedIndex));
    	}
    }
    
    @FXML
    void exitClick(ActionEvent event) throws IOException {
    	Parent userViewPage = FXMLLoader.load(Photos.class.getResource("/view/userViewPage.fxml"));
		FXMLLoader uvp = new FXMLLoader(Photos.class.getResource("/view/userViewPage.fxml"));
		
    	Scene scene = new Scene(userViewPage);
		userViewPageController controller = uvp.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }
    
    @FXML
    void logoutClick(ActionEvent event) throws IOException {
    	Parent loginPage = FXMLLoader.load(Photos.class.getResource("/view/loginPage.fxml"));
		Scene scene = new Scene(loginPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }
    
    
    ObservableList<String> tags = FXCollections.observableArrayList();

    
    
    public void setPhoto(Photo selectedPhoto){
    	try {
    		tags.clear();
    		Image newImage = new Image(selectedPhoto.imagePath);
    		
    		selectedImage.setFitHeight(newImage.getHeight());
    		selectedImage.setFitWidth(newImage.getWidth());
    		selectedImage.setPreserveRatio(true);
    		selectedImage.setFitHeight(300);
        	selectedImage.setImage(newImage);
        	
        	caption.setText(selectedPhoto.caption);
        	captureDate.setText(selectedPhoto.date.toString());
        	for (int i = 0; i < selectedPhoto.tags.size(); i++) {
        		tags.add(selectedPhoto.tags.get(i));
        	}
        	tagList.setItems(tags);
    	} catch (Exception e) {
    		
    	}
    }
    
    
    public static void writeToFile(Album a) throws FileNotFoundException, IOException {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users/" + currentUser.username + "/" + a.albumName + ".bin"))) {
			oos.writeObject(a);
		}
		
    }
    
    public static void readFile(String s) throws FileNotFoundException, IOException, ClassNotFoundException {
    	try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(s)))) {
			currentUser.albumList.add((Album)ois.readObject());
		}
    	
    }

}

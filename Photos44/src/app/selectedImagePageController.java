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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;

public class selectedImagePageController {
	
	public static Account currentUser;
	public static Album currentAlbum;
	
    @FXML
    private ListView<Album> albumList;

    @FXML
    private ImageView selectedImage;

    @FXML
    private TextField caption;

    @FXML
    private TextField captureDate;

    @FXML
    private ListView<String> tagList;

    @FXML
    private ComboBox<String> tagType;

    @FXML
    private TextField tagName;
    
    @FXML
    private Button addTagButton;

    @FXML
    private Button removeTagButton;
    
    @FXML
    private CheckBox multipleTag;



    public void initialize() {
    	tagType.getItems().clear();
    	tagType.getItems().addAll("color", "location", "person");
    	for (int i = 0; i < userViewPageController.currentAlbum.albumPhotos.size(); i++) {
    		for (int j = 0; j < userViewPageController.currentAlbum.albumPhotos.get(i).tags.size(); j++) {
    			int iend = userViewPageController.currentAlbum.albumPhotos.get(i).tags.get(j).indexOf("=");
    			try {
    				String newTag = userViewPageController.currentAlbum.albumPhotos.get(i).tags.get(j).substring(0, iend);
    				if (!tagType.getItems().contains(newTag))
    					tagType.getItems().add(newTag);
    			} catch (Exception e) {
    				
    			}
    		}
    	}
    }
    
    
    ObservableList<String> tags = FXCollections.observableArrayList();
    
    @FXML
    void addTagClick(ActionEvent event){
    	String currentTag = tagType.getSelectionModel().getSelectedItem();
    	if (currentTag.length() != 0 && tagName.getText().length() != 0) {
	    	String newTag = currentTag + "=" + tagName.getText();
	    	if (!tagType.getItems().contains(currentTag))
				tagType.getItems().add(currentTag);
	    	boolean duplicate = false;
	    	if (!multipleTag.isSelected()) {
	    		for (int i = 0; i < tags.size(); i++) {
	    			int iend = tags.get(i).indexOf("=");
	    			if (currentTag.contentEquals(tags.get(i).substring(0, iend)))
	    				duplicate = true;
	    		}
	    	}
	    	if (!duplicate) {
		    	userViewPageController.selectedPhoto.tags.add(newTag);
		    	tags.add(newTag);
		    	tagType.getSelectionModel().clearSelection();
		    	tagName.setText("");
		    	tagList.setItems(tags);
	    	} else {
	    		Alert alert = new Alert(AlertType.ERROR, "Tag type is a multiple when it cannot be.", ButtonType.OK);
	    		alert.showAndWait();
	    	}
    	} else {
    		Alert alert = new Alert(AlertType.ERROR, "Must enter in both tag type and tag text.", ButtonType.OK);
    		alert.showAndWait();
    	}
    }
    
    @FXML
    void removeTagClick(ActionEvent event) throws Exception{
    	try {
    	int removeTag = tagList.getSelectionModel().getSelectedIndex();
    	tags.remove(removeTag);
    	userViewPageController.selectedPhoto.tags.remove(removeTag);
    	} catch (Exception e) {
    		Alert alert = new Alert(AlertType.ERROR, "Must enter in both tag type and tag text.", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

//    @FXML
//    void saveChangesClick(ActionEvent event) throws FileNotFoundException, IOException{
//    	// currently doesn't seem to be needed
//    }
    
    
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
        	try {
        		captureDate.setText(selectedPhoto.date.toString());
        	} catch (Exception e) {
        		
        	}
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

package app;

/**
 * Jason Chou and Ty Goldin
 * Group#44
 *
 */

import object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import app.selectedImagePageController;

public class userViewPageController {

	public static Account currentAccount;
	public static Album currentAlbum;
	public static Photo selectedPhoto;
	
    static ObservableList<Photo> photoList = FXCollections.observableArrayList();

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button addPhotoButton;

    @FXML
    private Button copyPhotoButton;
    
    @FXML
    private Button removePhotoButton;

    @FXML
    private Button captionPhotoButton;
    
    @FXML
    private Button movePhotoButton;

    @FXML
    private Button viewSlideShowButton;

    @FXML
    private ListView<Photo> albumPhotos; //changed from Album -> Photo might cause issue marking for later
    
    @FXML
    private Text albumStatus;
    
    @FXML
    private Button addTagButton;    
    
   
    public void shutDown() throws FileNotFoundException, IOException {
    	save();
    }
    
    ObservableList<Photo> items = FXCollections.observableArrayList();
    
    selectedImagePageController secondController;
    Stage anotherStage;
    Stage rebaseStage;
    
    public void initialize() throws IOException {
    	photoList.clear();
    	albumStatus.setText("Photo Library: "+ currentAlbum);
    	for (int i = 0; i < currentAlbum.albumPhotos.size(); i++) {
    		photoList.add(currentAlbum.albumPhotos.get(i));
    	}
    	
    	albumPhotos.setItems(photoList);
    	setList();
    	
    	
    	
    	anotherStage = new Stage();
    	FXMLLoader selectedImageLoader = new FXMLLoader(Photos.class.getResource("/view/selectedImagePage.fxml"));
    	Parent anotherRoot = selectedImageLoader.load();
        Scene anotherScene = new Scene(anotherRoot);
        anotherStage.setScene(anotherScene);
        anotherStage.show();
        secondController = (selectedImagePageController)selectedImageLoader.getController();
        
        try {
        	albumPhotos.getSelectionModel().select(0);
        	selectedPhoto = albumPhotos.getSelectionModel().getSelectedItem();
        	secondController.setPhoto(albumPhotos.getSelectionModel().getSelectedItem());
    	} catch (Exception e) {
    		
    	}
    }
    
    @FXML
    void listViewClick(MouseEvent event) {
    	try {
    		selectedPhoto = albumPhotos.getSelectionModel().getSelectedItem();
    		secondController.setPhoto(selectedPhoto);
    		
    	} catch (Exception e) {
    		
    	}
    }
    

    
    @FXML
    void addPhotoClick(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        
        
        
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            Photo temp = new Photo(file.toURI().toString(), null);
           
            temp.setDate(file);
            
            TextInputDialog dialog = new TextInputDialog("");
        	dialog.setTitle("Setting Caption");
        	dialog.setHeaderText("Enter your desired caption:");
        	dialog.setContentText("Caption:");
        	Optional<String> result = dialog.showAndWait();
        	result.ifPresent(name -> {
        	    temp.caption = name;
        	});
        	
            userViewPageController.currentAlbum.albumPhotos.add(temp);
            
            photoList.add(temp);
            
        	setList();
            
        	if (albumPhotos.getSelectionModel().getSelectedItem()==null) {
        		albumPhotos.getSelectionModel().select(0);
        		secondController.setPhoto(albumPhotos.getSelectionModel().getSelectedItem());
        	}
//            imageTest.setImage(image);
        }
        
    }
    
    @FXML
    void backClick(ActionEvent event) throws FileNotFoundException, IOException {
    	save();
    	
    	anotherStage.close();
    	
    	Parent userPage = FXMLLoader.load(Photos.class.getResource("/view/userPage.fxml"));
		Scene scene = new Scene(userPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }

    
    public static void writeToFile(Album a) throws FileNotFoundException, IOException {
    	try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users/" + currentAccount.username + "/" + a.albumName + ".bin"))) {
			oos.writeObject(a);
		}
		
    }
    
    public static void readFile(String s) throws FileNotFoundException, IOException, ClassNotFoundException {
    	try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(s)))) {
			currentAccount.albumList.add((Album)ois.readObject());
		}
    	
    	
    }
    @FXML
    void removePhotoClick(ActionEvent event) throws Exception{
    	photoList.remove(albumPhotos.getSelectionModel().getSelectedItem());
    	setList();
    	secondController.setPhoto(albumPhotos.getSelectionModel().getSelectedItem());
    }
    
    
    
    

    @FXML
    void captionPhotoClick(ActionEvent event) throws Exception {
    	if (albumPhotos.getSelectionModel().getSelectedItem()!= null){
	    	TextInputDialog dialog = new TextInputDialog("");
	    	 
	    	dialog.setTitle("Renaming Caption");
	    	dialog.setHeaderText("Enter your desired caption:");
	    	dialog.setContentText("Caption:");
	    	 
	    	Optional<String> result = dialog.showAndWait();
	    	 
	    	result.ifPresent(name -> {
	    	    albumPhotos.getSelectionModel().getSelectedItem().caption = name;
	    	});
	    	setList();
	    	secondController.setPhoto(albumPhotos.getSelectionModel().getSelectedItem());
    	}
    }
    
    @FXML
    void copyPhotoClick(ActionEvent event) throws IOException {
    	selectedPhoto = albumPhotos.getSelectionModel().getSelectedItem();
    	
    	save();
    	rebasePhotoPageController.currentUser = currentAccount;
    	rebasePhotoPageController.previousAlbum = currentAlbum;
    	rebasePhotoPageController.status = "copying";
		Parent rebasePhotoPage = FXMLLoader.load(Photos.class.getResource("/view/rebasePhotoPage.fxml"));
		FXMLLoader uvp = new FXMLLoader(Photos.class.getResource("/view/rebasePhotoPage.fxml"));
		
    	
    	
    	Scene scene = new Scene(rebasePhotoPage);
		rebasePhotoPageController controller = uvp.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		anotherStage.close();
    }

    @FXML
    void logoutClick(ActionEvent event) throws IOException {
    	save();
    	
    	anotherStage.close();
    	
    	Parent loginPage = FXMLLoader.load(Photos.class.getResource("/view/loginPage.fxml"));
		Scene scene = new Scene(loginPage);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }

    @FXML
    void movePhotoClick(ActionEvent event) throws FileNotFoundException, IOException {
    	selectedPhoto = albumPhotos.getSelectionModel().getSelectedItem();
    	
    	save();
    	rebasePhotoPageController.currentUser = currentAccount;
    	rebasePhotoPageController.previousAlbum = currentAlbum;
    	rebasePhotoPageController.status = "moving";
		Parent rebasePhotoPage = FXMLLoader.load(Photos.class.getResource("/view/rebasePhotoPage.fxml"));
		FXMLLoader uvp = new FXMLLoader(Photos.class.getResource("/view/rebasePhotoPage.fxml"));
		
    	
    	
    	Scene scene = new Scene(rebasePhotoPage);
		rebasePhotoPageController controller = uvp.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		anotherStage.close();
    }

    @FXML
    void viewSlideShowClick(ActionEvent event) throws IOException {
    	Parent slideShowPage = FXMLLoader.load(Photos.class.getResource("/view/slideShowPage.fxml"));
		FXMLLoader sspc = new FXMLLoader(Photos.class.getResource("/view/slideShowPage.fxml"));
		
    	
    	
    	Scene scene = new Scene(slideShowPage);
		userViewPageController controller = sspc.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		anotherStage.close();
		controller.currentAlbum = currentAlbum;
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
                    setText(photo.caption);
                    setGraphic(imageView);
                }
            }
        });
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

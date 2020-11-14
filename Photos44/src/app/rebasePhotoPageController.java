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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.userViewPageController;

public class rebasePhotoPageController {

	public static Account currentUser;
	
	public static String status;
	
	public static Album previousAlbum;
	
//	public static Photo copyPhoto;
	
    @FXML
    private ListView<Album> albumList;

    @FXML
    private TextField albumName;

    @FXML
    private TextField photoNumber;

    @FXML
    private TextField dateRange;

    @FXML
    private Text statusText;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text userStatus;

    @FXML
    private Button logoutButton;
    
    public void initialize() {
    	albumList.setItems(currentUser.albumList);
    	setStatus(status);
    	userStatus.setText("Photo Library: " + currentUser.username);
    	confirmButton.setVisible(true);
    	cancelButton.setVisible(true);
    }
    
    @FXML
    void onListClick(MouseEvent event) throws Exception {
    	try {
    		showAlbum();
    	} catch (Exception e) {
    		
    	}
    	
    }
    

    @FXML
    void cancelClick(ActionEvent event) throws Exception {
    	userViewPageController.currentAlbum = previousAlbum;
		userViewPageController.currentAccount = currentUser;
		
		Parent userViewPage = FXMLLoader.load(Photos.class.getResource("/view/userViewPage.fxml"));
		FXMLLoader uvp = new FXMLLoader(Photos.class.getResource("/view/userViewPage.fxml"));
		
    	
    	
    	Scene scene = new Scene(userViewPage);
		userViewPageController controller = uvp.getController();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
    }

    @FXML
    void confirmClick(ActionEvent event) throws Exception{
    	if (status.contentEquals("copying")) {
    		Photo copiedPhoto = new Photo(userViewPageController.selectedPhoto.imagePath, userViewPageController.selectedPhoto.caption);
    		copiedPhoto.tags.addAll(userViewPageController.selectedPhoto.tags);
    		copiedPhoto.date = userViewPageController.selectedPhoto.date;
    		albumList.getSelectionModel().getSelectedItem().albumPhotos.add(copiedPhoto);
    		writeToFile(albumList.getSelectionModel().getSelectedItem());
    		writeToFile(previousAlbum);
    	}
    	if (status.contentEquals("moving")) {
    		previousAlbum.albumPhotos.remove(userViewPageController.selectedPhoto);
    		albumList.getSelectionModel().getSelectedItem().albumPhotos.add(userViewPageController.selectedPhoto);
    		writeToFile(albumList.getSelectionModel().getSelectedItem());
    		writeToFile(previousAlbum);
    	}
    	
    	userViewPageController.currentAlbum = previousAlbum;
		userViewPageController.currentAccount = currentUser;
		
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

    
    void setStatus(String status) {
    	if (status.contentEquals("copying")) {
    		statusText.setText(status + " to selected album...");
    	}
    	if (status.contentEquals("moving")) {
    		statusText.setText(status + " to selected album...");
    	}
    	
    }
    
    
    void showAlbum() throws Exception {
    	Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    	albumName.setText(selectedAlbum.albumName);
    	photoNumber.setText("" +selectedAlbum.albumPhotos.size());
    	try {
    		
    		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
    		
    		
    		if(albumList.getSelectionModel().getSelectedItem().albumPhotos.size() == 1) {
    			Photo p = albumList.getSelectionModel().getSelectedItem().albumPhotos.get(0);
    			Date d = p.date;
    			dateRange.setText(format1.format(d) + "-" + format1.format(d));
    			return;
    		}
    		
    		Date min;
    		Date max;
    		ArrayList<Photo> ptr = albumList.getSelectionModel().getSelectedItem().albumPhotos;
    		
    		if(ptr.get(0).date.before(ptr.get(1).date)) {
    			min = ptr.get(0).date;
    			max = ptr.get(1).date;
    		}
    		else {
    			min = ptr.get(1).date;
    			max = ptr.get(0).date;
    		}
    		
    		for(int i = 2; i < ptr.size(); i++) {
    			if(ptr.get(i).date.after(max))
    				max = ptr.get(i).date;
    			else if(ptr.get(i).date.before(min))
    				min = ptr.get(i).date;
    		}
    		
    		dateRange.setText(format1.format(min) + "-" + format1.format(max));
    	}
    	catch(Exception e) {
    		
    	}
    	/**
    	 * add other fields here once Album object is complete
    	 * 
    	 */
    	
    }
    
    void clearDetails() {
    	albumName.setText("");
    	photoNumber.setText("");
    	dateRange.setText("");
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

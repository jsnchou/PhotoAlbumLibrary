package object;

/**
 * Account class
 * @author Jason Chou and Ty Goldin
 * Group#44
 *
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account {
	
	
	/**
	 * ObservableList of albums for this specific account
	 */
	public ObservableList<Album> albumList = FXCollections.observableArrayList(); 
	
	/**
	 * String of this specific account's username 
	 */
	public String username;
	
	/**
	 * String of this specific account's password
	 */
	public String password;
	
	/**
	 * Character representing this specific account's type
	 */
	public char type;
	
	/**
	 * Account object constructor
	 * @param username String username
	 * @param password String password
	 * @param type Character type
	 */
	public Account(String username, String password, char type) {
		this.username = username;
		this.password = password;
		this.type = type;
	}
	
	
	/**
	 *Account object toString
	 *@return returns username of the account
	 */
	public String toString() {
		return username;
	}
	
	
	
}

package object;

/**
 * AccountList class
 * @author Jason Chou and Ty Goldin
 * Group#44
 *
 */

import java.io.Serializable;
import java.util.ArrayList;

public class AccountList implements Serializable {
	
	/**
	 * Long ID for serialization
	 */
	private static final long serialVersionUID = 4033017482271211409L;
	
	/**
	 * ArrayList of all usernames in order
	 */
	public ArrayList<String> usernames;
	
	/**
	 * ArrayList of all passwords in order
	 */
	public ArrayList<String> passwords;
	
	/**
	 * ArrayList of all types in order
	 */
	public ArrayList<Character> type; 
	
	/**
	 * AccountList object constructor
	 */
	public AccountList() {
		this.usernames = new ArrayList<String>();
		this.passwords = new ArrayList<String>();
		this.type = new ArrayList<Character>();
	}
}

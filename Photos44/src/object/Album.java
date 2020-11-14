package object;

/**
 * Album class
 * @author Jason Chou and Ty Goldin
 * Group#44
 *
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{

	/**
	 * Long ID for serialization
	 */
	private static final long serialVersionUID = 4033017482271211409L;
	
	/**
	 * String of this specific album
	 */
	public String albumName;
	
	/**
	 * ArrayList of this album's photos
	 */
	public ArrayList<Photo> albumPhotos;
	
	/**
	 * Album object constructor
	 * @param albumName String of album
	 */
	public Album (String albumName) {
		this.albumName = albumName;
		this.albumPhotos = new ArrayList<Photo>();
	}
	
	/**
	 * Album object toString
	 * @return returns name of album
	 */
	public String toString() {
		return albumName;
	}
}

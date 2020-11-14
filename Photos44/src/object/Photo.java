package object;

/**
 * Photo class
 * @author Jason Chou and Ty Goldin
 * Group#44
 *
 */

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;


public class Photo implements Serializable{
	/**
	 * Long ID for serialization
	 */
	private static final long serialVersionUID = 4033017482271211409L;
	
	
	/**
	 * String of a path to image
	 */
	public String imagePath;
	
	/**
	 * String of caption
	 */
	public String caption;
	
	/**
	 * ArrayList of tags 
	 */
	public ArrayList<String> tags;
	
	/**
	 * Calendar representing when photo was modified
	 */
	public Calendar cal;
	
	/**
	 * Date representing when photo was modified
	 */
	public Date date;
	
	
	
	/**
	 * Photo object constructor
	 * @param imagePath String of the path to photo
	 * @param caption String of the caption
	 */
	public Photo(String imagePath, String caption) {
		this.imagePath = imagePath;
		this.caption = caption;
		this.tags = new ArrayList<>();
		
		
		this.cal = Calendar.getInstance();
		this.cal.set(Calendar.MILLISECOND, 0);
		
		
	}
	
	/**
	 * sets the date and calendar for when photo was last modified
	 * @param path File of the photo
	 */
	public void setDate(File path) {
		this.date = new Date(path.lastModified());
		this.cal.setTime(this.date);
	}
	

}

package userIdent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileControl {
	
	/**
	 * Copy a file
	 * @param from File
	 * @param to File
	 */
	public void copy(File from, File to) {
		try {
			to.createNewFile();
			FileOutputStream out = new FileOutputStream(to);
			FileInputStream in = new FileInputStream(from);
			try {
				in.getChannel().transferTo(0, 
						in.getChannel().size(), 
						out.getChannel());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	/**
	 * Copy given file to given path
	 * @param from File
	 * @param toPath String
	 */
	public void copy(File from, String toPath) {
		File to = new File(toPath);
		
		try {
			to.createNewFile();
			FileOutputStream out = new FileOutputStream(to);
			FileInputStream in = new FileInputStream(from);
			try {
				in.getChannel().transferTo(0, 
											in.getChannel().size(), 
											out.getChannel());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
}

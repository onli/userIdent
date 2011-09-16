package userIdent;
import java.io.File;


public class Options {
	public static String DBpath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator;
	public static String userFile = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"users.ndx";
	public static String mistralPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"bin"+File.separator+"";
	public static String mistralConfPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"cfg"+File.separator+"";
	public static String soundPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"sounds"+File.separator+"";
	public static String prmPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"prm"+File.separator+"";
	public static String lblPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"lbl"+File.separator+"";
	public static String lstPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"lst"+File.separator+"";
	public static String gmmPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"gmm"+File.separator+"";
	public static String ndxPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"data"+File.separator+"ndx"+File.separator+"";
	public static String logPath = System.getProperty("user.home") +File.separator+".userIdent"+File.separator+"log"+File.separator+"";
	
	public static boolean demo = false;
}

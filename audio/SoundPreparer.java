package audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import userIdent.Logger;

/**
 * StreamGobbler is a helperclass grabbed from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4, which
 * prevents the thread getting stucked (catching stdout und stderr) 
 *
 */
class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run() {
    	try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ( (line = br.readLine()) != null) { 
            	System.out.println(type + ">" + line);
            } 
        } catch (IOException ioe) {
          ioe.printStackTrace();  
        }
    }
}

public class SoundPreparer {
	private Logger logger = new Logger();
	
	public File removeSilence(File audio) {
		File FilewoSilence = null;
		try {
			FilewoSilence = File.createTempFile("userIdent", ".wav");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String command = "./removeSilence.sh "+audio.getAbsolutePath()+" "+FilewoSilence.getAbsolutePath();
		try {
			Process child = Runtime.getRuntime().exec(command);
			//wait for command
			StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream(), "ERROR");            
        
			// any output?
	        StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream(), "OUTPUT");
	            
	        // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
			child.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FilewoSilence;
	}
	
	public File recordKinect(boolean train) {
		String command = "";
		if (train) {
			command = "./recordKinect30.sh";
		} else {
			command = "./recordKinect.sh";
		}
		try {
			Process child = Runtime.getRuntime().exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream(), "ERROR");            
	        StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream(), "OUTPUT");
	        errorGobbler.start();
	        outputGobbler.start();
			child.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File("record.wav");
		
	}
	
	public File record(boolean train) {
		String command = "";
		if (train) {
			command = "./record30.sh";
		} else {
			command = "./record.sh";
		}
		try {
			Process child = Runtime.getRuntime().exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream(), "ERROR");            
	        StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream(), "OUTPUT");
	        errorGobbler.start();
	        outputGobbler.start();
			child.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File("record.wav");
		
	}
}

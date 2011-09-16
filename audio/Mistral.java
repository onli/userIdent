package audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import userIdent.Logger;
import userIdent.Options;
import userIdent.User;


public class Mistral {
	
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
	
	/**
	 * Trains a user in mistral
	 * @param user
	 */
	public void trainUser(User user) {
		File idFile = new File(Options.ndxPath + "demo.ndx");
		try {
			FileWriter fw = new FileWriter(idFile);	
			System.out.print(user.getName());
			fw.append(user.getName() +"\t\t" + user.getName());
		
			fw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	    String command = Options.mistralPath + "TrainTarget.exe " +
	    		"--config " + Options.mistralConfPath + "target_male_demo.cfg " +
	    		"--featureFilesPath " + Options.prmPath + " " +
				"--mixtureFilesPath " + Options.gmmPath + " " +
				"--targetIdList " + idFile + " " +  
	    		"--inputWorldFilename world_demo ";
	    System.out.println(command);
		
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
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void trainWorld(Vector<User> users) {
		try {
			FileWriter fw = new FileWriter(Options.lstPath + "world_demolist.lst");
			    
			for (User user: users) {
				System.out.println(user.getName());
				fw.append(user.getName() +"\r\n");
			}
			fw.flush();
			File worldList = new File(Options.lstPath + "world_demolist.lst");
			String command = Options.mistralPath + "TrainWorld.exe " +
							"--config " + Options.mistralConfPath + "TrainWorldInit.cfg " +
							"--inputFeatureFilename " + worldList.getAbsolutePath() + " " +
							"--featureFilesPath " + Options.prmPath + " " +
							"--mixtureFilesPath " + Options.gmmPath + " " +
							"--labelFilesPath " + Options.lblPath + " " +
							"--outputWorldFilename world_init_demo";
			System.out.println(command);
			Process child = Runtime.getRuntime().exec(command);
			//wait for command
			StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream(), "ERROR");            
        
	        // any output?
	        StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream(), "OUTPUT");
	            
	        // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
			child.waitFor();

		    command = Options.mistralPath + "TrainWorld.exe " +
			"--config " + Options.mistralConfPath + "TrainWorldFinal.cfg " +
			"--inputFeatureFilename " + worldList.getAbsolutePath() + " " +
			"--inputWorldFilename world_init_demo " +
			"--featureFilesPath " + Options.prmPath + " " +
			"--mixtureFilesPath " + Options.gmmPath + " " +
			"--labelFilesPath " + Options.lblPath + " " +
			"--outputWorldFilename world_demo";
		    
		    System.out.println(command);
		    child = Runtime.getRuntime().exec(command);
		    errorGobbler = new StreamGobbler(child.getErrorStream(), "ERROR");            
	        
	        // any output?
	        outputGobbler = new StreamGobbler(child.getInputStream(), "OUTPUT");
	            
	        // kick them off
	        errorGobbler.start();
	        outputGobbler.start();
			child.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Extract the features of the stored-before soundfile of a user
	 * First thing to do after silence-removal
	 * @param user
	 */
	public void extractFeatures(User user) {
		String command = Options.mistralPath + "sfbcep " +
						"-F pcm16 -f 16000 -p 19 -m -e -D -A -k 0 " +
						Options.soundPath + user.getName() + ".wav " +
						Options.prmPath + user.getName() + ".prm -v"; 

		System.out.println(command);
		
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
	}
	
	public void normalizeFeatures(User user) {
		String command = Options.mistralPath + "NormFeat.exe " +
		"--config " +Options.mistralConfPath + "NormFeat.cfg " +
		"--inputFeatureFilename " +user.getName() + " " +
		"--loadFeatureFileExtension .enr.prm " +
		"--featureFilesPath " + Options.prmPath + " " +
		"--saveFeatureFileExtension .norm.prm " +
		"--labelFilesPath " + Options.lblPath + " " +
		"--debug false --verbose true";
		
		System.out.println(command);
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
	}	
		
	public void normalizeEnergy(User user) {
		String command = Options.mistralPath + "NormFeat.exe " +
		"--config " +Options.mistralConfPath + "NormFeat_energy.cfg " +
		"--inputFeatureFilename " +user.getName() + " " +
		"--loadFeatureFileExtension .prm " +
		"--featureFilesPath " + Options.prmPath + " " +
		"--saveFeatureFileExtension .enr.prm " +
		"--featureServerMask 19 --debug false --verbose true";
		
		System.out.println(command);
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
	}
	
	/**
	 * Test the given sound against the user
	 * @param user
	 * @param sound
	 * @return double the likelihood
	 */
	public double testUser(User user, File sound) {
		User test = new User("test");
		
		//We first have to store the testfile in sound for the
		//coming feature-extraction
		File testFile = new File(Options.soundPath + "test.wav");
		try {
			FileOutputStream out = new FileOutputStream(testFile);
			FileInputStream in = new FileInputStream(sound);
			try {
				in.getChannel().transferTo(0, 
											in.getChannel().size(), 
											out.getChannel());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		this.extractFeatures(test);
		this.normalizeEnergy(test);
		this.normalizeFeatures(test);
		
		File idFile = new File(Options.ndxPath + "test.ndx");
		try {
			FileWriter fw = new FileWriter(idFile);
		
			System.out.println(user.getName());
			fw.write("test" +"\t");
			fw.append(user.getName());
		
			fw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		File result = null;
		try {
			result = File.createTempFile("userIdent", ".res");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String command = Options.mistralPath + "ComputeTest.exe " +
		"--config "+Options.mistralConfPath + "target_seg_male_demo.cfg " +
		"--featureFilesPath " + Options.prmPath + " " +
		"--mixtureFilesPath " + Options.gmmPath + " " +
		"--labelFilesPath " + Options.lblPath + " " +
		"--ndxFilename " + idFile.getAbsolutePath() + " " +  
		"--inputWorldFilename world_demo " +
		"--outputFilename " + result.getAbsolutePath();
		System.out.println(command);
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
		try {
			FileReader resReader = new FileReader(result);
			BufferedReader br = new BufferedReader(resReader);
			String resultValue="";
			String s;
			try {
				while((s=br.readLine())!=null) {
					resultValue +=s;
				}
				System.out.print(resultValue);
				String[] resTemp = resultValue.split(" ");
				return Double.parseDouble(resTemp[resTemp.length -1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("----------------------------------------");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("----------------------------------------");
		} 
		return 0.0;
	}
}

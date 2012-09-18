package edu.neu.ccs.kemf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class SystemUtil {
	
	private static final String TARGET_DIR = "target";

	/**
	 * Execute a command and return weather it fails or not
	 * @param cmd The list of command to execute
	 * @return True if command succeeds false otherwise
	 * @throws IOException
	 */
	public static boolean executeCmd(List<String> cmd) throws IOException {
		return (executeCmdGetReturn(cmd, false) == "");		
	}
	
	/**
	 * Execute a comand on the system and get the result
	 * @param cmd The list of command to execute
	 * @return A string with the result
	 * @throws IOException
	 */
	public static String executeCmdGetReturn(List<String> cmd) throws IOException {
		return executeCmdGetReturn(cmd, true);		
	}
	
	/**
	 * Execute a comand on the system and get the response
	 * @param cmd Command list to execute
	 * @param readResult true to return string result false return empty string
	 * @return The resonse from the command
	 * @throws IOException
	 */
	private static String executeCmdGetReturn(List<String> cmd, boolean readResult) throws IOException {
		Process process = runCommand(cmd);
		
		// Need to read output stream otherwise fills up and deadlocks
		// get stderr and stdout 
	    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String result = "";	
	    String lineRead;
	    while ((lineRead = br.readLine()) != null) {
	        // ignore line..
	    	if (readResult)
	    		result += lineRead + "\n";
	    }

		// wait until finishes and save exit status
		int exitVal = -1;
		try {
			exitVal = process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!(exitVal == 0))
			throw new IOException(readStreamToString(process.getErrorStream()));
		
	    return result;
	}
	
	/**
	 * Run a command and return the process spawnned
	 * @param cmd The list of comand to run
	 * @return A new process for the command
	 * @throws IOException
	 */
	public static Process runCommand(List<String> cmd) throws IOException {
		//System.out.println(Arrays.toString(cmd.toArray()));
		Process process = null;
		
		// create the procress builder
		ProcessBuilder pb = new ProcessBuilder(cmd);
		pb.redirectErrorStream(true);
		
		// run the command
		process = pb.start();
		
		return process;
	}
	
	private static String readStreamToString(InputStream stream) {
		StringBuilder output = new StringBuilder();
		char[] buffer = new char[1024];
		Reader streamReader;
		try {
			streamReader = new InputStreamReader(stream, "UTF-8");
			int read = 0;
			while (read >= 0) {			
				read = streamReader.read(buffer, 0, buffer.length);
				if (read > 0) {
					output.append(buffer, 0, read);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output.toString();		
			
	}
	
	public static String getTargetFilePath(String fileName) {
		File targetDir = new File(System.getProperty("user.dir") + File.separator + TARGET_DIR);
		if (targetDir.exists()) {
			File[] files = targetDir.listFiles();
			for (File file : files) {
				if (file.getName().equalsIgnoreCase(fileName))
					return file.getPath();
			}
		}
		return null;
	}
	
}

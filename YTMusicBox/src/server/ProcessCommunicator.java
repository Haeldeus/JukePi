package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessCommunicator {

	
	static public String parseStandardURL(String url) throws IOException{
		Process parseProcess = new ProcessBuilder("youtube-dl","-g", url).start();
		BufferedReader parseInput = new BufferedReader(new InputStreamReader(parseProcess.getInputStream()));
		IO.printlnDebug(null, "waiting for output url...");
		String parsedURL = parseInput.readLine();
		parseInput.close();
		return parsedURL;
	}
	
	public static String parseTitle(String url) throws IOException{
		Process parseProcess = new ProcessBuilder("youtube-dl","-e", url).start();
		BufferedReader parseInput = new BufferedReader(new InputStreamReader(parseProcess.getInputStream()));
		IO.printlnDebug(null, "waiting for output title...");
		String parsedURL = parseInput.readLine();
		parseInput.close();
		return parsedURL;		
	}
	
	/**
	 * plays the video of the given parsed url and returns, when done
	 * 
	 * @param parsedURL the parsed youtube url that has to be played
	 * @throws IOException 
	 */
	static public Process getExternPlayerProcessk(String parsedURL){
		try {
			return  new ProcessBuilder("omxplayer",parsedURL).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

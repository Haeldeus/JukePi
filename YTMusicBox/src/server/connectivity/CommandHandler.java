package server.connectivity;

import java.io.BufferedWriter;

import messages.MessageType;
import server.YTJBServer;
import server.connectivity.commands.*;
import utilities.IO;

/**handles incomming connections
 * 
 * @author Mellich
 *
 */
public class CommandHandler extends Thread {
	
	public static final String FILESAVELOCATION = "files/";
	public static final String TEMPDIRECTORY = "/tmp/";
	
	private YTJBServer server;
	private BufferedWriter out;
	private String message;
	private Connection parent;
	
	public CommandHandler(BufferedWriter out,YTJBServer server,Connection parent,String message) {
		this.out = out;
		this.server = server;
		this.message = message;
		this.parent = parent;
	}
	
	private void handleCommand(String[] args){
		int prompt = Integer.parseInt(args[0]);
		IO.printlnDebug(this, "Parsing input...");
		switch (prompt){
		case MessageType.PAUSERESUME: new PauseResumeCommand(out,prompt,server).handle();
			break;
		case MessageType.SKIP: new SkipCommand(out,prompt,server.getScheduler()).handle();
			break;
		case MessageType.GAPYOUTUBE: new YoutubeCommand(out,prompt,server,false,false,args[1]).handle();
			break;
		case MessageType.GAPLISTSAVETOFILE: new SaveGapListCommand(out,prompt,server).handle();
			break;
		case MessageType.DELETEFROMGAPLIST: new DeleteFromListCommand(out,prompt,server,false,Integer.parseInt(args[1])).handle();
			break;
		case MessageType.GETGAPLIST: new GetListCommand(out,prompt, server,false).handle();
			break;
		case MessageType.GETWISHLIST: new GetListCommand(out,prompt, server,true).handle();
			break;
		case MessageType.YOUTUBE:  new YoutubeCommand(out,prompt,server,true,false,args[1]).handle();
			break;
		case MessageType.ISREADY: new CheckIfReadyCommand(out,prompt).handle();
			break;
		case MessageType.GETCURRENTTRACK: new GetCurrentTrackCommand(out,prompt,server.getScheduler()).handle();
			break;
		case MessageType.GETCURRENTPLAYBACKSTATUS: new GetCurrentPlaybackStatusCommand(out,prompt,server.getScheduler()).handle();
			break;
		case MessageType.BEGINNINGYOUTUBE: new YoutubeCommand(out,prompt,server,true,true,args[1]).handle();
			break;
		case MessageType.GAPBEGINNINGYOUTUBE: new YoutubeCommand(out,prompt,server,false,true,args[1]).handle();
			break;
		case MessageType.DECLAREMEASNOTIFY: server.registerNotifiable(parent);;
			break;
		case MessageType.GAPLISTTRACKUP: new GapListTrackUpCommand(out,prompt,server,Integer.parseInt(args[1])).handle();
			break;
		case MessageType.GAPLISTTRACKDOWN: new GapListTrackUpCommand(out,prompt,server,Integer.parseInt(args[1]) + 1).handle();
			break;	
		case MessageType.GETAVAILABLEGAPLISTS: new GetGapListsCommand(out,prompt,server).handle();
			break;
		case MessageType.LOADGAPLIST: new ChangeGapListCommand(out,prompt,server,args[1]).handle();
			break;
		case MessageType.GETCURRENTGAPLISTNAME: new GetCurrentGapListNameCommand(out,prompt,server).handle();
			break;
		case MessageType.GETTITLEFROMGAPLIST: new GetTitleOfGapListCommand(out,prompt,server,args[1]).handle();
			break;
		case MessageType.SETMEASPLAYER: server.registerPlayer(parent);
			break;
		case MessageType.GETNEXTVIDEOURL: new GetNextVideoURLCommand(out,prompt, server.getScheduler()).handle();
			break;
		case MessageType.PLAYERFINISHED: new PlayerFinishedCommand(out,prompt, server).handle();
			break;
		case MessageType.DELETEGAPLIST: new DeleteGapListCommand(out, prompt,server, args[1]).handle();
			break;
		case MessageType.GETLOADGAPLISTSTATUS: new GetLoadGapListStatusCommand(out, prompt, server).handle();
			break;
		case MessageType.GETCURRENTCLIENTCOUNT: new GetCurrentClientCountCommand(out,prompt,server).handle();
			break;
		case MessageType.GETCURRENTPLAYERCOUNT: new GetCurrentPlayerCountCommand(out,prompt,server).handle();
			break;
		case MessageType.SEEKFORWARD: new SeekForwardCommand(out,prompt,server.getScheduler()).handle();
			break;
		case MessageType.SEEKBACKWARD: new SeekBackwardCommand(out,prompt,server.getScheduler()).handle();
			break;
		default: new UnknownCommand(out,MessageType.NOTIMPLEMENTEDCOMMANDNOTIFY,""+prompt).handle();
		}		
	}
	
	@Override
	public void run() {
		super.run();
		handleCommand(message.split(MessageType.SEPERATOR));
	}


}

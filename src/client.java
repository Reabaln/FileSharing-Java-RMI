
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * this is the client side of the file sharing system (FSS) implement using RMI
 * @author Reab Alnitaifi
 */
public class client implements Serializable {
	
	static Registry registry;
	static RMIinterface rInterface;

	
	public static void main(String[] args)  {
		String envVar = System.getenv("PA1_SERVER");
		String [] parts = envVar.split(":");
		String address = parts[0];
		int port = Integer.parseInt(parts[1]);
		if (args.length<1) {System.out.println("Invalid command");
		System.exit(1);}
		System.out.println("Sending Request to IP address= " + address + " , Port Number= " + port);
		
	
		
		try {
		registry = LocateRegistry.getRegistry(address, port);				
		rInterface = (RMIinterface) registry.lookup("remoteObj");

		String userCommand = "";
		//get command and command string arguments
		if (args != null && args.length > 0 && !args[0].trim().isEmpty()) { //check for blank lines
			userCommand = args[0].trim();	//the command given
			
			switch (userCommand) {
			case "upload":
				System.out.println("Upload Selected - Client");
				if (args.length==3) {
				do_put(args[1].toString().trim(),args[2].toString().trim() );}
				else
					System.out.println("Invalid command, USAGE(command + path on client+ path on server )");

				break;
				
			case "download":
				System.out.println("Download Selected - Client");
				if (args.length==3) {
				do_get(args[1].toString().trim(),args[2].toString().trim() );}
				else
					System.out.println("Invalid command, USAGE(command + path on server + path on client)");
				break;
			case "dir":
				System.out.println("List Directory Content Selected - Client");
				if (args.length==2) {
				do_dir(args[1].toString().trim());}
				else
					System.out.println("Invalid command, USAGE(command + Directory Path )");
				break;
				
			case "mkdir":
				System.out.println("Create New Directory Selected - Client");
				if (args.length==2) {
				do_mkdir(args[1].toString().trim());}
				else
					System.out.println("Invalid command, USAGE(command + Directory Path )");
				break;
				
			case "rmdir":
				System.out.println("Remove  Directory Selected - Client");
				if (args.length==2) {
				do_rmdir(args[1].toString().trim());}
				else
					System.out.println("Invalid command, USAGE(command + Directory Path )");
				break;
			case "shutdown":
				do_shutdown();
			break;
				
				
			default:
				System.out.println("Invalid command.");
				
			} //End switch
		}// End if
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	catch (NotBoundException ex ) {
			ex.printStackTrace();

		}

	
	}



	/**
	 * upload a specific file to server
	 * @param pathOnClient File path on client machine 
	 * @param pathOnServer File path on server
	 * @throws RemoteException
	 */
	private static void do_put(String pathOnClient, String pathOnServer) throws RemoteException {
		String fname = pathOnClient.trim();
		String filed= pathOnServer.trim();
		System.out.println("Searching file name= " + fname);
		File outFile = new File(fname);
		////////////////////
		if (outFile.exists()) {			
			System.out.println("File Found ....");
			System.out.println("File is being sent to path= " + filed);	
			try {
			byte [] mybytearray  = new byte [(int) outFile.length()];
			FileInputStream fInStream = new FileInputStream(outFile);
			fInStream.read(mybytearray,0,mybytearray.length);
			rInterface.do_put(mybytearray,filed, (int) outFile.length());
			fInStream.close();
			System.out.println("File uploaded");
			} catch (Exception e) {
		         e.printStackTrace();
			}
		}
		else {
			System.out.println("File " + fname + " does not exist.");
		}
		}// End do_put() 
	
	/**
	 * Retrieve a specific file from server
	 * @param pathOnServer File path on server
	 * @param pathOnClient File path on client machine
	 * @throws RemoteException 
	 */
	private static void do_get(String pathOnServer, String pathOnClient) throws RemoteException {
		String fname = pathOnClient.trim();
		String filed= pathOnServer.trim();
		byte [] data  = new byte [rInterface.getFileSize(filed)];
		System.out.println("Request is being sent to RMI....");
		data = rInterface.do_get(filed);
		File clientFile = new File(fname);
		try {
			FileOutputStream cFile = new FileOutputStream (clientFile);
			cFile.write(data);
			cFile.flush();
			cFile.close();
			System.out.println("File downloaded");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
			
	}// end do_get
	
	/**
	 * list the directory content
	 * @param dirPath path of directory to be listed
	 * @throws RemoteException
	 */
	private static void do_dir(String dirPath) throws RemoteException {
		String dPath = dirPath;
		System.out.println("Request is being sent to RMI....");
		String [] filesNameList =  rInterface.do_dir(dPath);
		System.out.println("Number of files= " + filesNameList.length );	
		for (int i=0 ; i< filesNameList.length; i++)
		{
			System.out.println(filesNameList[i]);
		}
		
	} // End do_dir()
	
	
	/**
	 * create new directory in server
	 * @param newDirPath path of the new directory
	 * @throws RemoteException
	 */
	private static void do_mkdir(String newDirPath) throws RemoteException {
		String message;
		String newDirectory = newDirPath; 
		message = rInterface.do_mkdir(newDirectory);
		System.out.println(message);
	} // end do_mkdir()
	/**
	 * sends remove directory request to server
	 * @param dirPath directory path to be removed
	 * @throws RemoteException
	 */
	private static void do_rmdir(String dirPath)throws RemoteException {
		String message;
		String directoryPath = dirPath; 
		message = rInterface.do_rmdir(directoryPath);
		System.out.println(message);
		
	}//end do_rmdir()
	/**
	 * sends shutdown request to server
	 * @throws RemoteException
	 */
	private static void do_shutdown() throws RemoteException {
		try {
		System.out.println("Shutting down RMI Server");
		rInterface.do_shutdown();
		}
		 catch (Exception e) {
			}
		
	}


}

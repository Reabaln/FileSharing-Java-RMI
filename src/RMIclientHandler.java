import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * this the the implementation of all methods provided to the client - implemented using RMI
 * @author Reabaln
 */

public class RMIclientHandler extends UnicastRemoteObject implements RMIinterface, Serializable{

	/**
	 * Used during deserialization 
	 */
	private static final long serialVersionUID = 1L;
	Registry registry;
	
	/**
	 * Constructor for the remote object
	 * @param s the file directory 
	 * @throws RemoteException. Object export could potentially throw a java.rmi.RemoteException
	 */
	protected RMIclientHandler(String s, Registry r) throws RemoteException {
		File serverStorage = new File (s);
		serverStorage.mkdir();
		this.registry=r;
	}
	
	/**
	 * Receiving a file form client 
	 * @param mydata
	 * @param serverpath
	 * @param length
	 * @throws RemoteException
	 */
	public void do_put(byte[] data, String dirPath, int fileLength) throws RemoteException {
		 
		try {
			File inFile = new File(dirPath);
			FileOutputStream fos = new FileOutputStream(inFile);
			byte [] content =data;	
    		fos.write(content);
			fos.flush();
	    	fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) { 
			e.printStackTrace();
		}
	
	}
	
	/**
	 * return the size of a specific file
	 * @param filePath
	 * @throws RemoteException
	 */
	public int getFileSize (String filePath) throws RemoteException {
		int size;
		File myFile = new File(filePath);
		size = (int)myFile.length();
		return size;

				
	}
	/** return a specific file in form byte[]
	 * @param pathOnServer file path.
	 * @throws RemoteException
	 */
	public byte[] do_get (String pathOnServer) throws RemoteException {
		File toClient = new File (pathOnServer)	;
		System.out.println("Searching file name= " + pathOnServer);
		byte[] data = new byte[getFileSize(pathOnServer)];	
		try {
			if (toClient.exists()) {
			FileInputStream fis = new FileInputStream(toClient);
			fis.read(data, 0, data.length);	
			fis.close(); 
			}
			else {
				System.out.println("File " + pathOnServer + " does not exist.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		return data;
	}

	/**
	 * Returns an array of abstract pathnames denoting the files in the directory 
	 * denoted by this abstract pathname.
	 * @param pathOnServer file path.
	 * @throws RemoteException
	 */
	public String[] do_dir(String pathOnServer) throws RemoteException {
		String sDir = pathOnServer;
		File dir = new File(sDir);
		if (dir.isDirectory() && dir.exists()) {
			System.out.println("directory path found");	
		}
		else {
			System.out.println("directory path is not a directory path or doesn't exist");	
		}
		return dir.list();
	}// end do_dir()
	
	public String do_mkdir (String pathOnServer) throws RemoteException{
		String directoryPath = pathOnServer;
		String message;
		File dir = new File(directoryPath);
		if (dir.exists()) {
			
            if (dir.list().length>0) {
				message ="Directory Path Already Exist";
			}
			else {
				if (dir.delete()) message= "Directory Path was empty and has been deleted";		
				else
				message ="Error deleting existing empty file";					
			} 
			
		} else 
		{
			dir.mkdir();
			message ="Directory has been created successfully";						
		}
		return message;		
	}

	/**
	 * removes a directory if its a directory. if the directory is not empty delete 
	 * all the files inside the directory along with the directory itself 
	 * @param PathOnServer directory path to be deleted 
	 */
	public String do_rmdir(String pathOnServer) throws RemoteException {
		String directoryPath = pathOnServer;
		String message;
		
			File dir = new File(directoryPath);
			if (!dir.isDirectory()) {
				message = "Invalid directory";	}	
			else {
				
			if(dir.list().length>0) {

			File[] filesList = dir.listFiles();
			//Deleting Directory Content
			for(File file : filesList){
				System.out.println("Deleting "+file.getName());
				file.delete();
			}}
			if (dir.delete()) message ="Successfully deleted the Directory: " + directoryPath ;
			else message ="Error deleting the directory: " + directoryPath ;
			}//else end 
		return message;
	}

	/**
	 * shutdown the RMI server
	 */
	public void do_shutdown() throws RemoteException {
		try{
			
 
	        System.out.println("File Server shutdown requested.");
	        //Removes the binding for the specified name in this registry. 
	        registry.unbind("remoteObj");
	        // Removes the remote object from the RMI runtime
            if (UnicastRemoteObject.unexportObject(registry, false))  System.out.println("Server has been terminated successfully");
			System.exit(1);

	    }
	    catch(Exception e){
			e.printStackTrace();

	    }

	}
}

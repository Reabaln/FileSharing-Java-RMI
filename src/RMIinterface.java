import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * this is a remote interface that declares each of the methods to be called remotely. 
 * @author Reabaln
 */

public interface RMIinterface extends Remote{
	public void do_put(byte[] data, String dirPath, int fileLength) throws RemoteException ;
	public int getFileSize (String filePath) throws RemoteException;
	public byte[] do_get (String pathOnServer) throws RemoteException;
	public String[] do_dir (String pathOnServer) throws RemoteException;
	public String do_mkdir (String pathOnServer) throws RemoteException;
	public String do_rmdir (String pathOnServer) throws RemoteException;
	public void do_shutdown () throws RemoteException;
	

}

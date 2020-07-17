# FileSharing-Java-RMI

File sharing using Java Remote Method Invocation

### Running server
	java -cp name.jar server start <portnumber>
  
### Client commands
Following list of commands that client can perform:

##### Upload file
	java -cp name.jar client upload <path_on_client> </path/filename/on/server>
  
##### Download file
	java -cp name.jar client download </path/existing_filename/on/server> <path_on_client>
	
##### List directory content
	java -cp name.jar client dir  </path/existing_directory/on/server> 

##### Make new direcroty 
	ava -cp name.jar client mkdir </path/new_directory/on/server>
	
##### Remove directory
	java -cp name.jar client rmdir </path/existing_directory/on/server>
	
##### Remove file 
	ava -cp name.jar client rm </path/existing_filename/on/server>

##### Shoutdown client
	ava -cp name.jar client shutdown

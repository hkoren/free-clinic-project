FREE CLINIC PROJECT

SERVER SUBPROJECT    -  org.freeclinic.server.*
-----------------------------------------------
This is Java based server that listens on a TCP socket for SSL connections.

It manages a connection to a database, and interacts with the android client
through multiple threads.

Building the server:
--------------------
	ant jar

   Requires Apache Ant http://ant.apache.org/

Starting the server:
--------------------

    type ./start.sh  from unix/cygwin
           start.bat from windows 
           
Kill, Update from SVN, Build, and re-launch the server:

		./recycle
		
Server Configuration:
---------------------
  TCP Listen port defined in:
  
      org.freeclinic.server.Server.port 
 /src/org/freeclinic/server/Server.java
	         
	TODO: Server conf file
	         
	         
	
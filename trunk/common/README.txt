FREE CLINIC PROJECT

COMMON SUBPROJECT    -  org.freeclinic.common.*
-----------------------------------------------
This project defines objects which are exchanged between the client 
subproject and the server subproject.

Building common:
----------------
	ant jar

   Requires Apache Ant http://ant.apache.org/

   Output: FreeClinicCommon.jar
   
Using Common:
-------------
You can use common by adding the jar file to your classpath for your project.

Including in org.freeclinic.android:
------------------------------------
Set as project reference in eclipse.

Including in org.freeclinic.server:
-----------------------------------
This file is copied automatically by ant to the assets/ subfolder when the
server is built. 
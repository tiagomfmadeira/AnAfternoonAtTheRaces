javac src/interfaces/*.java src/registry/*.java src/main/*.java src/settings/*.java src/sharedRegions/*.java src/entities/*.java

# Registry
cp src/interfaces/Register.class dir_registry/interfaces/
cp src/registry/*.class dir_registry/registry/
cp src/settings/*.class dir_registry/registry/settings/

# Server side
cp src/interfaces/*.class dir_serverSide/interfaces/
cp src/main/*Server.class src/sharedRegions/*.class src/settings/*.class  dir_serverSide/serverSide/
cp src/settings/*.class dir_serverSide/serverSide/settings/

# Client side
cp src/interfaces/I*.class  dir_clientSide/interfaces/
cp src/entities/*.class src/settings/*.class dir_clientSide/clientSide/
cp src/settings/*.class dir_clientSide/clientSide/settings/

# Public 
mkdir -p /home/sd0406/Public/classes
mkdir -p /home/sd0406/Public/classes/interfaces
mkdir -p /home/sd0406/Public/classes/clientSide

cp src/interfaces/*.class /home/sd0406/Public/classes/interfaces
cp src/entities/*.class /home/sd0406/Public/classes/clientSide
cp set_rmiregistry.sh /home/sd0406
cp set_rmiregistry_alt.sh /home/sd0406
~                                          

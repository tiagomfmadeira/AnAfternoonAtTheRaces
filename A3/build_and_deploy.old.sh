#!/usr/bin/env bash
javac src/interfaces/*.java src/registry/*.java serverSide/*.java clientSide/*.java
cp interfaces/Register.class dir_registry/interfaces/
cp registry/*.class dir_registry/registry/
cp interfaces/*.class dir_serverSide/interfaces/
cp serverSide/*.class dir_serverSide/serverSide/
cp interfaces/Compute.class interfaces/Task.class dir_clientSide/interfaces/
cp clientSide/*.class dir_clientSide/clientSide/
mkdir -p /home/sd0406/Public/classes
mkdir -p /home/sd0406/Public/classes/interfaces
mkdir -p /home/sd0406/Public/classes/clientSide
cp interfaces/*.class /home/sd0406/Public/classes/interfaces
cp clientSide/Pi.class /home/sd0406/Public/classes/clientSide
cp set_rmiregistry.sh /home/sd0406
cp set_rmiregistry_alt.sh /home/sd0406

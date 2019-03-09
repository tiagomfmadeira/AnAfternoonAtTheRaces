#!/usr/bin/env bash
javac src/interfaces/*.java src/states/*.java  src/registry/*.java src/settings/*.java src/serverSide/*/*.java src/clientSide/*/*.java src/serverSide/*.java src/serverSide/*.java

# Registry
#
cp src/interfaces/*.class dir_registry/interfaces/
cp src/registry/*.class dir_registry/registry/
cp src/settings/*.class dir_registry/settings/
cp src/states/*.class dir_registry/states/


# Server side
#
cp src/interfaces/*.class dir_serverSide/interfaces/

for sharedRegion in "bettingCenter" "controlCenter" "generalRepository" "paddock" "raceTrack" "stable"; do 
    mkdir -p dir_serverSide/serverSide/$sharedRegion/
	cp src/serverSide/$sharedRegion/*.class  dir_serverSide/serverSide/$sharedRegion/
done

cp src/settings/*.class dir_serverSide/settings/
cp src/states/*.class dir_serverSide/states/


# Client side
#
cp src/interfaces/*.class  dir_clientSide/interfaces/

for entity in "broker" "horseJockey" "spectator"; do
	mkdir -p dir_clientSide/clientSide/$entity/ 
    cp src/clientSide/$entity/*.class  dir_clientSide/clientSide/$entity/
done

cp src/settings/*.class dir_clientSide/settings/
cp src/states/*.class dir_clientSide/states/



# Public 
#
mkdir -p /home/sd0406/Public/classes
mkdir -p /home/sd0406/Public/classes/interfaces
mkdir -p /home/sd0406/Public/classes/settings
mkdir -p /home/sd0406/Public/classes/states
mkdir -p /home/sd0406/Public/classes/clientSide

cp src/interfaces/*.class /home/sd0406/Public/classes/interfaces
cp src/settings/*.class /home/sd0406/Public/classes/settings
cp src/states/*.class /home/sd0406/Public/classes/states

for entity in "broker" "horseJockey" "spectator"; do
    mkdir -p /home/sd0406/Public/classes/clientSide/$entity
done


for entity in "broker" "horseJockey" "spectator"; do
    rsync -a src/clientSide/$entity/*.class /home/sd0406/Public/classes/clientSide/$entity --exclude=*Main*
done

cp set_rmiregistry.sh /home/sd0406
cp set_rmiregistry_alt.sh /home/sd0406


#!/usr/bin/env bash

declare -A sharedRegionLocation=(   [bettingCenter]=2
                                    [controlCenter]=3
                                    [paddock]=4
                                    [raceTrack]=5
                                    [stable]=6
                                )

declare -A entityLocation=( [broker]=7
                            [horseJockey]=8
                            [spectator]=9
                        )

for sharedRegion in "${!sharedRegionLocation[@]}"; do
    ws="${sharedRegionLocation[$sharedRegion]}"

    zip -r dir_serverSide_"$sharedRegion".zip dir_serverSide/ -x "*.sh" -x "*log*" -x "dir_serverSide/serverSide/*"
    zip -ur dir_serverSide_"$sharedRegion".zip dir_serverSide/"$sharedRegion"* dir_serverSide/serverSide/"$sharedRegion"/

    scp -r dir_serverSide_"$sharedRegion".zip sd0406@l040101-ws0"$ws".ua.pt:/home/sd0406/
	scp -r remoteScripts/unpack_remote_server.sh sd0406@l040101-ws0"$ws".ua.pt:/home/sd0406/

    rm dir_serverSide_"$sharedRegion".zip
done


for entity in "${!entityLocation[@]}"; do
    ws="${entityLocation[$entity]}"

    zip -r dir_clientSide_"$entity".zip dir_clientSide/ -x "*.sh" -x "*log*" -x "dir_clientSide/clientSide/*"
    zip -ur dir_clientSide_"$entity".zip dir_clientSide/"$entity"* dir_clientSide/clientSide/"$entity"/

    scp -r dir_clientSide_"$entity".zip sd0406@l040101-ws0"$ws".ua.pt:/home/sd0406/
	scp -r remoteScripts/unpack_remote_client.sh sd0406@l040101-ws0"$ws".ua.pt:/home/sd0406/
    
	rm dir_clientSide_"$entity".zip
done




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

ssh  sd0406@l040101-ws01.ua.pt<< EOF
	./unpack.sh
    cd /home/sd0406/A3
    ./build_and_deploy.sh
    ./deploy_remote.sh
    cd
    nohup ./set_rmiregistry.sh 22450 > /dev/null  &
    sleep 1
    cd /home/sd0406/A3/dir_registry
    nohup ./registry_com.sh > /dev/null  &
    sleep 1
    cd /home/sd0406/A3/dir_serverSide
    nohup ./generalRepository_com.sh > /dev/null &
	
EOF
 
for sharedRegion in "${!sharedRegionLocation[@]}"; do

	ws="${sharedRegionLocation[$sharedRegion]}"
	name="$sharedRegion"
	
	ssh sd0406@l040101-ws0"$ws".ua.pt<< EOF
		./unpack_remote_server.sh
		cd /home/sd0406/dir_serverSide
		nohup ./${name}_com.sh  > /dev/null  &
EOF
done

for entity in "${!entityLocation[@]}"; do

    ws="${entityLocation[$entity]}"
	name="$entity"

    ssh sd0406@l040101-ws0"$ws".ua.pt<< EOF
        ./unpack_remote_client.sh
        cd /home/sd0406/dir_clientSide
		nohup  ./${name}_com.sh > /dev/null  &
EOF
done



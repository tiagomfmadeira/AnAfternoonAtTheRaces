#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///home/sd0406/A3/dir_clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     spectator.SpectatorMain

#!/usr/bin/env bash
java -Djava.rmi.server.codebase="file:///home/sd0406/A3/dir_serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.controlCenter.ControlCenterServer

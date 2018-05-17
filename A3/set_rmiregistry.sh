#!/usr/bin/env bash
rmiregistry -J-Djava.rmi.server.codebase="http://192.168.8.171/sd0406/classes/"\
            -J-Djava.rmi.server.useCodebaseOnly=true $1

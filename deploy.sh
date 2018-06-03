zip A3.zip -r A3 -x "*run.sh*" -x "*kill.sh*"
scp A3.zip sd0406@l040101-ws01.ua.pt:/home/sd0406/
scp A3/remoteScripts/unpack.sh  sd0406@l040101-ws01.ua.pt:/home/sd0406/
rm A3.zip

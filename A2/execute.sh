ssh sd0406@l040101-ws01.ua.pt "java -jar ~/src/*.jar"  &  
sleep 1
ssh sd0406@l040101-ws02.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws03.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws04.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws05.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws06.ua.pt "java -jar ~/src/*.jar"  &
# replaced by ws10 until ws7 is available 
ssh sd0406@l040101-ws10.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws08.ua.pt "java -jar ~/src/*.jar"  &
ssh sd0406@l040101-ws09.ua.pt "java -jar ~/src/*.jar"  &

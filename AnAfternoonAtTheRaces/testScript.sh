#!/bin/bash
#usage: testScrypt.sh <n_iterations>
for ((i=1;i<=$1;i++)); do
    java -classpath /home/silverio/SD/afternoon_races/AnAfternoonAtTheRaces/out/production/afternoon_races:/home/silverio/SD/genclass.jar main.MainDatatype
done

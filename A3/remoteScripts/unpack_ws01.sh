if [ -e A3.zip ];then 
    rm -rf A3
    ./clean.sh
    unzip -o A3.zip
    chmod -R 700 A3
    rm A3.zip
fi


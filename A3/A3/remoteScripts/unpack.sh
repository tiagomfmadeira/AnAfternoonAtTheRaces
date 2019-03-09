if [ -e A3.zip ];then 
    rm -rf A3
    rm set*.sh
    unzip -o A3.zip
    chmod 700 A3
    chmod u+x **/*.sh
	rm A3.zip
fi


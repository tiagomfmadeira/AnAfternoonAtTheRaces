#!/usr/bin/env bash
if [ -e *.zip ];then
    rm -rf dir_clientSide
    unzip -o *.zip
    rm *.zip
fi


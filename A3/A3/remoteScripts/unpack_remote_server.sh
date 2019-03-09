#!/usr/bin/env bash
if [ -e *.zip ];then
    rm -rf dir_serverSide
    unzip -o *.zip
    rm *.zip
fi


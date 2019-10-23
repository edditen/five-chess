#!/usr/bin/env bash


PORT=$(jps -l | grep ChessServer | awk '{print $1}')
echo "stop server: $PORT"
kill -9 $PORT


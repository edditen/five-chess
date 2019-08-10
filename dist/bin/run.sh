#!/usr/bin/env bash

JAVA_HOME=$JAVA_HOME
JAVA="$JAVA_HOME/bin/java"

BIN_DIR="${BASH_SOURCE-$0}"
BIN_DIR="$(dirname "${BIN_DIR}")"
BASE_DIR="$(cd "${BIN_DIR}"/..; pwd)"
CFG_DIR="$(cd "${BASE_DIR}"/conf; pwd)"
CFG_FILE="$CFG_DIR"/app.properties

APP_MAIN="com.tenchael.chess.ChessServer"

CLASSPATH="$CFG_DIR:$CLASSPATH"
CLASSPATH="$BASE_DIR/classes:$CLASSPATH"

# for i in "$BASE_DIR"/lib/*.jar
# do
#     CLASSPATH="$i:$CLASSPATH"
# done



JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m"
JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8  -XX:-UseParNewGC"
JAVA_OPT="${JAVA_OPT} -verbose:gc -Xloggc:$BASE_DIR/logs/gc.log -XX:+PrintGCDetails"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
JAVA_OPT="${JAVA_OPT}  -XX:-UseLargePages"
JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${BASE_DIR}/lib"
#JAVA_OPT="${JAVA_OPT} -Xdebug -Xrunjdwp:transport=dt_socket,address=9555,server=y,suspend=n"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
JAVA_OPT="${JAVA_OPT} -Dapp.config=${CFG_FILE}"
JAVA_OPT="${JAVA_OPT} -cp ${CLASSPATH}"
JAVA_OPT="${JAVA_OPT} $APP_MAIN"


$JAVA ${JAVA_OPT} $@

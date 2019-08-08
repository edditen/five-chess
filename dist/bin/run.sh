#!/usr/bin/env bash

JAVA_HOME=$JAVA_HOME
JAVA="$JAVA_HOME/bin/java"

BINDIR="${BASH_SOURCE-$0}"
BINDIR="$(dirname "${BINDIR}")"
BASEDIR="$(cd "${BINDIR}"/..; pwd)"
CFGDIR="$(cd "${BASEDIR}"/conf; pwd)"

APP_MAIN="com.tenchael.chess.ChessServer"

CLASSPATH="$CFGDIR:$CLASSPATH"
CLASSPATH="$BASEDIR/classes:$CLASSPATH"

for i in "$BASEDIR"/lib/*.jar
do
    CLASSPATH="$i:$CLASSPATH"
done



JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m"
JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+CMSClassUnloadingEnabled -XX:SurvivorRatio=8  -XX:-UseParNewGC"
JAVA_OPT="${JAVA_OPT} -verbose:gc -Xloggc:/tmp/five-chess/gc.log -XX:+PrintGCDetails"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
JAVA_OPT="${JAVA_OPT}  -XX:-UseLargePages"
JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${BASE_DIR}/lib"
#JAVA_OPT="${JAVA_OPT} -Xdebug -Xrunjdwp:transport=dt_socket,address=9555,server=y,suspend=n"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
JAVA_OPT="${JAVA_OPT} -cp ${CLASSPATH}"
JAVA_OPT="${JAVA_OPT} $APP_MAIN"




$JAVA ${JAVA_OPT} $@

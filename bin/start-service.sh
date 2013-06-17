#!/bin/bash
#
#  start lodil test
#
#  @author tangfl
#


[ -z $CURR_DIR ] && CURR_DIR="`pwd`"
[ -z $BIN_DIR ] && SCRIPT_PATH="`greadlink -f $0`" && PRG_DIR="`dirname $SCRIPT_PATH`" \
    && BIN_DIR="`cd $PRG_DIR; pwd`" && unset PRG_DIR SCRIPT_PATH
[ -z $BASE_DIR] && BASE_DIR=`cd $BIN_DIR/../; pwd`
LIB_PATH=$BASE_DIR/lib

JAVA=`which java`

LOCALIP=`/sbin/ifconfig | awk '/inet / {if(match($2,"addr")) {print substr($2,6)} else {print $2}}' | head -2 |tail -1`

# memory
X_P_XMS=1g
X_P_XMX=1g
X_P_XMN=100m
X_P_XSS=512k

JAVA_OPTS="-Xms$X_P_XMS -Xmx$X_P_XMX -Xmn$X_P_XMN -Xss$X_P_XSS $JAVA_OPTS"


# print gc
JAVA_OPTS="-XX:+PrintTenuringDistribution \
    -XX:+PrintGCDetails -XX:+PrintGCTimeStamps \
    -XX:+PrintGCApplicationStoppedTime \
    -XX:+PrintGCApplicationConcurrentTime \
    -Xloggc:$CURR_DIR/logs/gc.log \
    $JAVA_OPTS"
   
# gc turning    
#JAVA_OPTS="–XX:MaxGCPauseMillis=10 -XX:+UseCompressedOops $JAVA_OPTS"
JAVA_OPTS="-XX:CMSInitiatingOccupancyFraction=80 $JAVA_OPTS"
JAVA_OPTS="-XX:+UseConcMarkSweepGC -XX:SurvivorRatio=6 -XX:MaxTenuringThreshold=5 $JAVA_OPTS"

# for debug
JPDA_ADDRESS=18083
#JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=$JPDA_ADDRESS,server=y,suspend=n $JAVA_OPTS"

PNAME="lodil"

process=`ps -ef | grep "pname=$PNAME " | grep -v grep`
if [[ ! -z $process ]]; then
	echo "process already running!"
	echo
	ps -ef | grep "pname=$PNAME"
	exit
fi

JAVA_OPTS="-Dfiledir=../data/ $JAVA_OPTS"
JAVA_OPTS="-Dmapsize=8192000 $JAVA_OPTS"
JAVA_OPTS="-Dtestsize=2000 $JAVA_OPTS"

MAIN_CLASS="com.weibo.lodil.mmap.wrap.MmapKVDictionary"
LOG_PATH="$BASE_DIR/logs/stdout-$PNAME.log"
mv -f $LOG_PATH $LOG_PATH.bak 2>/dev/null

## build CLASSPATH
if [[ -z $CLASSPATH ]]; then
    CLASSPATH=.
fi

CLASSPATH=$CLASSPATH:$LIB_PATH/lodil.jar
for file in $LIB_PATH/*.jar
do
    CLASSPATH=$CLASSPATH:$file
done

OUTPUT_PATH="$BASE_DIR/target/classes/"
if [[ -d ${OUTPUT_PATH} ]]; then
    CLASSPATH=$CLASSPATH:${OUTPUT_PATH}
fi

export CLASSPATH=$CLASSPATH

## for profile usage
#JAVA_OPTS="$JAVA_OPTS -javaagent:$CURR_DIR/log/profile.jar \
#    -Dprofile.properties=$OUTPUT_PATH/profile.properties"

### start process
nohup $JAVA -Dpname=$PNAME -Dpwd=$CURR_DIR $JAVA_OPTS -server \
    -Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8 -classpath $CLASSPATH \
    $MAIN_CLASS >$LOG_PATH 2>&1 &

pid=$!

process=`ps -ef | grep $pid | grep -v grep`
if [[ -z $process ]]; then
    echo "failed to start process $pid !"
    exit
fi

ps -ef | grep $pid

echo
echo $pid > $BASE_DIR/data/$PNAME.pid 2>/dev/null

echo "$PNAME ($CURR_DIR) [$pid] is now running ... "

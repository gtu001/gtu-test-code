#!/bin/bash

export DERBY_HOME_PARAM="/media/gtu001/OLD_D/apps/db_tool/db-derby-10.12.1.1-bin"

export DERBY_HOME="${DERBY_HOME_PARAM}"

java -jar ${DERBY_HOME}/lib/derbyrun.jar server start

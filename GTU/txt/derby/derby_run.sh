#!/bin/bash

#/media/gtu001/OLD_D/apps/db_tool/db-derby-10.12.1.1-bin/bin/

DERBY_HOME=$(dirname "$0")

export DERBY_HOME_PARAM="${DERBY_HOME}"

export DERBY_HOME="${DERBY_HOME}"

java -jar ${DERBY_HOME}/lib/derbyrun.jar server start

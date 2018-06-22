

SET DERBY_HOME_PARAM="C:\Users\wistronits\Downloads\db-derby-10.12.1.1-bin\db-derby-10.12.1.1-bin"

setx DERBY_HOME "%DERBY_HOME_PARAM%" /m

java -jar %DERBY_HOME%\lib\derbyrun.jar server start

PAUSE
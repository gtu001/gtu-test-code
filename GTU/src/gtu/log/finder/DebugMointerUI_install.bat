cd c:

cd C:\Users\gtu001\Desktop\my_tool

mvn install:install-file -Dfile=DebugMointerUI.jar -DgroupId=gtu.log.finder -DartifactId=DebugMointerUI -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true

copy DebugMointerUI.jar c:\Oracle\Middleware\user_projects\domains\base_domain\lib\DebugMointerUI.jar /Y

copy DebugMointerUI.jar C:\workspace\workspace_farEastStone\estore\fet_estore_search_engie_revamp\revamp_source_code\lib\include\DebugMointerUI.jar /Y

pause
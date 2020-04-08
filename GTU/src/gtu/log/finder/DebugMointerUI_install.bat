cd "%~dp0"

"D:\JavaEnv4\apache-maven-3.6.0\bin\mvn.cmd" install:install-file -Dfile=DebugMointerUI.jar -DgroupId=gtu.log.finder -DartifactId=DebugMointerUI -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true

REM copy DebugMointerUI.jar c:\Oracle\Middleware\user_projects\domains\base_domain\lib\DebugMointerUI.jar /Y

REM copy DebugMointerUI.jar C:\workspace\workspace_farEastStone\estore\fet_estore_search_engie_revamp\revamp_source_code\lib\include\DebugMointerUI.jar /Y

pause
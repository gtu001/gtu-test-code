set JAVA_LIB=".;mysql-connector-java-5.1.35.jar;ojdbc7.jar;sqljdbc4-4.1.jar;db2jcc.jar;derbyclient.jar;postgresql-42.2.3.jar;"

java -cp .;%JAVA_LIB%;FastDBQueryUI.exe gtu._work.ui.FastDBQueryUI


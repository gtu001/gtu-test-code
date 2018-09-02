
REM mongod --dbpath ./data/db --logpath ./data/mongod.log --port 27017 --logappend --fork --setParameter enableTestCommands=1

set mongod="E:\apps\db_tool\mongodb-3.6.2\bin\mongod.exe"
set mongo_config="E:\apps\db_tool\mongodb-3.6.2\mongod.cfg"


%mongod%  --config  %mongo_config%  --install



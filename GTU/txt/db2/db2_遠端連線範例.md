DB2遠端連線範例

   db2 catalog tcpip node <node_name> remote <host_name_or_address> server <port_no_or_service_name>
   db2 catalog dcs database <local_name> as <real_db_name> 
   db2 catalog database <local_name> as <alias> at <node node_name> authentication server


Ex : jdbc:db2://10.95.40.1:50000/CXL1
	id: dbaa
	pwd: cathayaa


   db2 catalog tcpip node CXL1N remote 10.95.40.1 server 50000
   -- db2 catalog dcs database CXL1L as CXL1R <--貌似不用
   db2 catalog database CXL1 as CXL1A2 at node CXL1N authentication server
   db2 connect to  CXL1A2  user  dbaa   using   cathayaa

                        
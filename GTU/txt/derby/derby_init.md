derby_init.md
---
	在 derby_home 底下
	$ export DERBY_HOME=$(dirname "$0")
	$ java -jar ./lib/derbyrun.jar ij
	$ CONNECT 'jdbc:derby:seconddb;create=true';
	$ create schema test;



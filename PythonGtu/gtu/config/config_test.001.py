import configparser as cparser
import sys


config = cparser.ConfigParser()
ret = config.read("config.ini")
if len(ret) < 1:
    #failed
    sys.exit(-1)

for section in config.sections():
    print ("In section [%s]" % section)
    params = config.items(section)
    for (k,v) in params:
        print ("  Key '%s' has value '%s'" % (k, v))

if config.has_option('Section_B', 'alarm'):
    value = config.get('Section_B', 'alarm')
    print("Section_B->Alarm = ", value)

config2 = cparser.ConfigParser()
ret = config2.read("config2.ini")
if not config2.has_section("Section_A"):
    config2.add_section("Section_A")
config2.set("Section_A", "Key_ABC", "GOGOGO")#要修改Key 的 Value
config2.write(open('config2.ini', 'w'))
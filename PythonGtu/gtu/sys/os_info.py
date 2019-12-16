import platform
import sys

'''
from gtu.sys import os_info
'''

def linux_distribution():
    try:
        print("""Python version: %s 
            dist: %s
            linux_distribution: %s
            system: %s
            machine: %s
            platform: %s
            uname: %s
            version: %s
            mac_ver: %s
          """ % (
            sys.version.split('\n'),
            str(platform.dist()),
            linux_distribution(),
            platform.system(),
            platform.machine(),
            platform.platform(),
            platform.uname(),
            platform.version(),
            platform.mac_ver(),
        ))
        return platform.linux_distribution()
    except:
        return "N/A"


def isWindows() :
  import platform, sys, os
  # os.name 输出字符串指示正在使用的平台。如果是window 则用'nt'表示，对于Linux/Unix用户，它是'posix'。
  if os.name == 'nt' :
    return True
  elif os.name == 'posix' :
    return False

  if str(sys.platform).lower().startswith("win") \
      and str(platform.platform()).lower().startswith("win") :
      return True
  return False


def getPathEnd() :
  if isWindows() :
    return ";"
  return ":"


if __name__ == '__main__' :
#     scrapy_runner.runSpider(PttBeautyCrawlerSpider)
    print("isWindows", isWindows())
    
#     linux_distribution()
    print("done...")

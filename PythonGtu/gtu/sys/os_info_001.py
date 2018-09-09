import platform
import sys


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




if __name__ == '__main__' :
#     scrapy_runner.runSpider(PttBeautyCrawlerSpider)
    import sys
    print(sys.platform)
    
    import platform
    print(platform.platform())
    
#     linux_distribution()
    print("done...")
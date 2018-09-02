#!/usr/bin/python
# Filename: use_logging.py
import os,platform,logging

if platform.platform().startswith('Windows'):
    logging_file = os.path.join(os.getenv('HOMEDRIVE'), 
                                os.getenv('HOMEPATH'), 'test.log')
else:
    logging_file = os.path.join(os.getenv('HOME'), 'test.log')

print("logging_file - ", logging_file)
    
logging.basicConfig(level=logging.DEBUG,\
                    format='%(asctime)s : %(levelname)s : %(message)s',\
                    filename = logging_file,\
                    filemode = 'w',
                    )

class LogConfig :
    def __init__(self):
        # create file handler which logs even debug messages
        self.fh = logging.FileHandler('spam.log')
        self.fh.setLevel(logging.DEBUG)
        # create console handler with a higher log level
        self.ch = logging.StreamHandler()
        self.ch.setLevel(logging.DEBUG)
        # create formatter and add it to the handlers
        self.formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        self.fh.setFormatter(self.formatter)
        self.ch.setFormatter(self.formatter)

    def apply(self, logger, isFileOn=False):
        # add the handlers to the logger
        logger.addHandler(self.ch)
        
        if isFileOn :
            logger.addHandler(self.fh)
        

    def createLogger(self, name):
        # create logger with 'spam_application'
        logger = logging.getLogger(name)
        logger.setLevel(logging.DEBUG)
        return logger



if __name__ == '__main__' :
    logConf = LogConfig()
    logger = logConf.createLogger("test logger")
    logConf.apply(logger, True)
    
    logger.debug("Start of the program")
    logger.info("Doing something")
    logger.warning("Dying now")
    print("done...")

package gtu.log.line;

import gtu.log.line.LoggerFactoryGtu.Logger;

public class MainTest {

    private static final Logger logger = LoggerFactoryGtu.getLogger();

    public static void main(String[] args) {
        logger.debug("vvvvvvvvvvvvvvv");
        logger.line("tttttttt");
        logger.info("done...");
    }

}

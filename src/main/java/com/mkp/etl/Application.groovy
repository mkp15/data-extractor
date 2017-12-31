package com.mkp.etl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by pandma on 12/31/17.
 */
@SpringBootApplication
class Application implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application, args)
    }

    @Override
    void run(ApplicationArguments args) throws Exception {
        logger.info("i am here")
    }
}

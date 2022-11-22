package com.estee;

import com.gaia.config.TimeInterceptor;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@Log4j2
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.gaia", "com.estee"})
@MapperScan("com.estee.dao")
public class ChaosApplication {
    @Value("/api")
    private String path;

    public static void main(String[] args) {
        SpringApplication.run(ChaosApplication.class, args);
    }

    @Bean
    public void print(){
        log.info("~~~~~~~~~~~     ~~~~~~~~~~");
        log.info("   @@@@@@@        @@@@@@  ");
        log.info(" @@@@@@@@@@@    @@@@@@@@@@ ");
        log.info(" @@@@@@@@@@@    @@@@@@@@@@ ");
        log.info("   @@@@@@@        @@@@@@  ");
        log.info("$$$$$$$$$$$$$:"+path);
    }

    @Bean
    public TimeInterceptor getInterceptor() {
        return new TimeInterceptor();
    }
}

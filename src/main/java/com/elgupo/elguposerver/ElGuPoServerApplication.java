package com.elgupo.elguposerver;

import com.elgupo.elguposerver.postrequester.ActualEventsHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElGuPoServerApplication {

    public static void main(String[] args) {
        ActualEventsHolder.getInstance();
        SpringApplication.run(ElGuPoServerApplication.class, args);
    }

}

package com.elgupo.elguposerver;

import com.elgupo.elguposerver.database.LikesCleaner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.elgupo.elguposerver.postrequester.*;

@SpringBootApplication
public class ElGuPoServerApplication {

    public static void main(String[] args) {
        ActualEventsHolder.getInstance();
        new LikesCleaner();
        SpringApplication.run(ElGuPoServerApplication.class, args);
    }

}

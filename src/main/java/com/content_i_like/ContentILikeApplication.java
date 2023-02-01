package com.content_i_like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContentILikeApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ContentILikeApplication.class);
        springApplication.setBanner((enviroment, sourceClass, out) ->{
            out.println("\n"
                + "                                                                                            \n"
                + " ,-----.                 ,--.                   ,--.      ,--.    ,--.   ,--.,--.           \n"
                + "'  .--./ ,---. ,--,--, ,-'  '-. ,---. ,--,--, ,-'  '-.    |  |    |  |   `--'|  |,-. ,---.  \n"
                + "|  |    | .-. ||      \\'-.  .-'| .-. :|      \\'-.  .-'    |  |    |  |   ,--.|     /| .-. : \n"
                + "'  '--'\\' '-' '|  ||  |  |  |  \\   --.|  ||  |  |  |      |  |    |  '--.|  ||  \\  \\\\   --. \n"
                + " `-----' `---' `--''--'  `--'   `----'`--''--'  `--'      `--'    `-----'`--'`--'`--'`----' \n"
                + "                                                                                            \n");
        });
        springApplication.run(args);
        //SpringApplication.run(ContentILikeApplication.class, args);
    }

}

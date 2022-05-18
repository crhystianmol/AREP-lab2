package com.masiv.callCenter.spring;

import com.masiv.callCenter.beans.Agent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
@SpringBootApplication
public class Main {
    public static void main( String[] args )
    {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("com/masiv/xml/beans.xml");
        Agent a = appContext.getBean(Agent.class);
        System.out.println(a.getName());
        SpringApplication.run(Main.class, args);
    }
}

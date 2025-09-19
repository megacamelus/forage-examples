package com.foo.acme;

import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period=1000")
                .to("sql:select * from bar")
                .log("from sql default ds - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from bar"))
                .to("jdbc:dataSource")
            .log("from jdbc default ds - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from bar"))
                .to("spring-jdbc:dataSource")
                .log("from spring-jdbc default ds - ${body}");
    }
}


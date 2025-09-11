package com.foo.acme;

import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;

@Component
public class Test extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period=1000")
                .to("sql:select * from acme?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.DefaultDataSourceFactory")
                .log("from sql default ds - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from acme"))
                .to("jdbc:dataSource?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.DefaultDataSourceFactory")
                .log("from jdbc default ds - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from acme"))
                .to("spring-jdbc:dataSource?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.DefaultDataSourceFactory")
                .log("from spring-jdbc default ds - ${body}");
    }
}

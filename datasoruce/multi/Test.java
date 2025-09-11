package com.foo.acme;

import org.apache.camel.builder.RouteBuilder;

public class Test extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period=1000")
            .to("sql:select * from acme?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory&dataSource=#ds1")
            .log("from sql postgres - ${body}")
            .to("sql:select * from test.foo?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory&dataSource=#ds2")
            .log("from sql mysql - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from acme"))
                .to("jdbc:ds1?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory")
                .log("from jdbc postgres - ${body}")
                .setBody(constant("select * from test.foo"))
                .to("jdbc:ds2?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory")
                .log("from jdbc mysql - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from acme"))
                .to("spring-jdbc:ds1?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory")
                .log("from spring jdbc postgres - ${body}")
                .setBody(constant("select * from test.foo"))
                .to("spring-jdbc:ds2?dataSourceFactory=#class:org.apache.camel.forage.jdbc.factory.MultiDataSourceFactory")
                .log("from spring jdbc mysql - ${body}");
    }
}

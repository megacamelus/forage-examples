package com.foo.acme;

import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:producer?period=5000")
                .setBody(constant("Hello from Camel Forage JMS!"))
                .to("jms:queue:test.queue")
                .log("Message sent to JMS queue");

        from("jms:queue:test.queue")
                .log("Message received from JMS queue: ${body}");
    }
}

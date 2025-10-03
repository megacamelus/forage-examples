import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period=1000")
                .to("sql:select * from bar?dataSource=#ds1")
                .log("from sql postgresql - ${body}")
                .to("sql:select * from test.foo?dataSource=#ds2")
                .log("from sql mysql - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from bar"))
                .to("jdbc:ds1")
                .log("from jdbc postgresql - ${body}")
                .setBody(constant("select * from test.foo"))
                .to("jdbc:ds2")
                .log("from jdbc mysql - ${body}");

        from("timer:java?period=1000")
                .setBody(constant("select * from bar"))
                .to("spring-jdbc:ds1")
                .log("from spring jdbc postgresql - ${body}")
                .setBody(constant("select * from test.foo"))
                .to("spring-jdbc:ds2")
                .log("from spring jdbc mysql - ${body}");
    }
}

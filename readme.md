# How to build and run a Camel application

This project was generated using [Camel Jbang](https://camel.apache.org/manual/camel-jbang.html). Please, refer the the online documentation for learning more about how to configure the export of your Camel application.

This is a brief guide explaining how to build, "containerize" and run your Camel application.

## Build the Maven project

```bash
./mvnw clean package
```

The application could now immediately run:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Create a Docker container

You can create a container image directly from the `src/main/docker` resources. Here you have a precompiled base configuration which can be enhanced with any further required configuration.

```bash
docker build -f src/main/docker/Dockerfile -t multi-agent:1.0-SNAPSHOT .
```

Once the application is published, you can run it directly from the container:

```bash
docker run -it multi-agent:1.0-SNAPSHOT
```


# Exporting to Quarkus:

Run:

```
./export-multi-agent.sh
```

Then modify the files (or apply the patch [ details at the end ])

Step 1:

Add the following dependencies:

```
        <!-- override -->
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-langchain4j-agent-api</artifactId>
            <version>4.14.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-langchain4j-agent</artifactId>
            <version>4.14.1-SNAPSHOT</version>
        </dependency>
```

Note: this is because CEQ is still using 4.14.0 which is missing some fixes available on 4.14.1-SNAPSHOT

Step 2

```
        <dependency>
            <groupId>org.apache.camel.quarkus</groupId>
            <artifactId>camel-quarkus-langchain4j-tools</artifactId>
        </dependency>
```

This is necessary because this extension brings `org.apache.camel.quarkus:camel-quarkus-support-langchain4j` which seems to force Quarkus' Langchain4j:

```
[INFO] +- org.apache.camel.quarkus:camel-quarkus-langchain4j-tools:jar:3.26.0:compile
[INFO] |  +- org.apache.camel.quarkus:camel-quarkus-support-langchain4j:jar:3.26.0:compile
[INFO] |  |  \- io.quarkiverse.langchain4j:quarkus-langchain4j-core:jar:1.1.3:compile
[INFO] |  |     +- io.quarkus:quarkus-jackson:jar:3.27.0.CR1:compile
[INFO] |  |     |  +- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.19.2:compile
[INFO] |  |     |  +- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:jar:2.19.2:compile
[INFO] |  |     |  \- com.fasterxml.jackson.module:jackson-module-parameter-names:jar:2.19.2:compile
[INFO] |  |     +- io.quarkus:quarkus-qute:jar:3.27.0.CR1:compile
[INFO] |  |     |  \- io.quarkus.qute:qute-core:jar:3.27.0.CR1:compile
[INFO] |  |     +- io.quarkus:quarkus-vertx:jar:3.27.0.CR1:compile
[INFO] |  |     |  +- io.quarkus:quarkus-netty:jar:3.27.0.CR1:compile
[INFO] |  |     |  |  +- io.netty:netty-codec:jar:4.1.127.Final:compile
[INFO] |  |     |  |  |  \- io.netty:netty-common:jar:4.1.127.Final:compile
[INFO] |  |     |  |  +- io.netty:netty-codec-http:jar:4.1.127.Final:compile
[INFO] |  |     |  |  +- io.netty:netty-codec-http2:jar:4.1.127.Final:compile
[INFO] |  |     |  |  \- io.netty:netty-handler:jar:4.1.127.Final:compile
[INFO] |  |     |  |     +- io.netty:netty-resolver:jar:4.1.127.Final:compile
[INFO] |  |     |  |     \- io.netty:netty-transport-native-unix-common:jar:4.1.127.Final:compile
[INFO] |  |     |  +- io.netty:netty-codec-haproxy:jar:4.1.127.Final:compile
[INFO] |  |     |  |  +- io.netty:netty-buffer:jar:4.1.127.Final:compile
[INFO] |  |     |  |  \- io.netty:netty-transport:jar:4.1.127.Final:compile
[INFO] |  |     |  +- io.smallrye.common:smallrye-common-vertx-context:jar:2.13.8:compile
[INFO] |  |     |  |  \- io.vertx:vertx-core:jar:4.5.21:compile
[INFO] |  |     |  |     +- io.netty:netty-handler-proxy:jar:4.1.127.Final:compile
[INFO] |  |     |  |     |  \- io.netty:netty-codec-socks:jar:4.1.127.Final:compile
[INFO] |  |     |  |     \- io.netty:netty-resolver-dns:jar:4.1.127.Final:compile
[INFO] |  |     |  |        \- io.netty:netty-codec-dns:jar:4.1.127.Final:compile
[INFO] |  |     |  +- io.quarkus:quarkus-mutiny:jar:3.27.0.CR1:compile
[INFO] |  |     |  |  +- io.quarkus:quarkus-smallrye-context-propagation:jar:3.27.0.CR1:compile
[INFO] |  |     |  |  |  \- io.smallrye:smallrye-context-propagation:jar:2.2.1:compile
[INFO] |  |     |  |  |     +- io.smallrye:smallrye-context-propagation-api:jar:2.2.1:compile
[INFO] |  |     |  |  |     \- io.smallrye:smallrye-context-propagation-storage:jar:2.2.1:compile
[INFO] |  |     |  |  \- io.smallrye.reactive:mutiny-smallrye-context-propagation:jar:2.9.4:compile
[INFO] |  |     |  +- io.quarkus:quarkus-virtual-threads:jar:3.27.0.CR1:compile
[INFO] |  |     |  +- io.quarkus:quarkus-vertx-latebound-mdc-provider:jar:3.27.0.CR1:compile
[INFO] |  |     |  +- io.smallrye.reactive:smallrye-mutiny-vertx-core:jar:3.19.2:compile
[INFO] |  |     |  |  +- io.smallrye.reactive:smallrye-mutiny-vertx-runtime:jar:3.19.2:compile
[INFO] |  |     |  |  \- io.smallrye.reactive:vertx-mutiny-generator:jar:3.19.2:compile
[INFO] |  |     |  |     \- io.vertx:vertx-codegen:jar:4.5.21:compile
[INFO] |  |     |  \- io.smallrye:smallrye-fault-tolerance-vertx:jar:6.9.2:compile
```

The tools dependency is already defined on the camel-langchain4j-agent component, so there is no harm done here.


Step 3

Add `langchain4j.version` property:

```
<langchain4j.version>1.3.0</langchain4j.version>
```

Add `langchain4j-bom`:


```
            <dependency>
                <groupId>dev.langchain4j</groupId>
                <artifactId>langchain4j-bom</artifactId>
                <version>${langchain4j.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```

And the langchain4j dependencies:

```
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>

        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-core</artifactId>
            <version>${langchain4j.version}</version>
        </dependency>
```

Note: for reasons I don't yet fully understand, the code seems to try to use LangChain4j 1.1.0 which is old and doesn't have all the APIs that we need.



## Alternative: apply the patch

If you just want to test something, you can do:

`cd ceq-example && patch < ../patches/ceq.patch`

Note: tested on macOs only
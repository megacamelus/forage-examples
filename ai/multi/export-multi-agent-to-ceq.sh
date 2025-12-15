camel export --runtime=quarkus \
	--dir=ceq-example multi-agent.camel.yaml forage-agent-factory.properties \
	--dep=mvn:org.apache.camel.forage:forage-agent:1.0-SNAPSHOT \
	--dep=mvn:org.apache.camel.forage:forage-memory-message-window:1.0-SNAPSHOT \
	--dep=mvn:org.apache.camel.forage:forage-model-google-gemini:1.0-SNAPSHOT \
	--dep=mvn:org.apache.camel.forage:forage-model-ollama:1.0-SNAPSHOT \
	--dep=camel-langchain4j-agent

cp ceq-example

# then, to run:
# cd ceq-example
# mvn -Dcamel.version=4.14.1-SNAPSHOT quarkus:dev

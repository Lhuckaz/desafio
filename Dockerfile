FROM maven:3.6.3-jdk-8 
COPY . .
RUN mvn clean package

# Extend vert.x image
FROM vertx/vertx3

ENV VERTICLE_NAME com.primeiropay.desafio.MainVerticle
ENV VERTICLE_FILE target/desafio-1.0.0-SNAPSHOT.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8888

# Copy your verticle to the container
COPY --from=0 $VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
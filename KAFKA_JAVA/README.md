Install Kafka and Zookeper Docker Compose

#Create and Start Container
docker-compose up -d

#List Container
docker ps -a

#Create topic
docker exec -it <kafka-container-id> /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic --from-beginning

#Produce Message
docker exec -it <kafka-container-id> /opt/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic

#Consume Message
docker exec -it <kafka-container-id> /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic --from-beginning


#Stop and Remove Container
docker-compose down

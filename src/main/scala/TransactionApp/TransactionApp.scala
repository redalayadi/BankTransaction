package TransactionApp

import java.util.Properties
import java.util.concurrent.TimeUnit

import Serdes.StreamSerde
import com.typesafe.config.ConfigFactory
import models.Transaction
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder

object TransactionApp extends App {

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.Serdes._

  val conf = ConfigFactory.load
  implicit val transactionSerde = new StreamSerde().transactionSerde

  val props = new Properties
  props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("bootstrap.servers"))
  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, conf.getString("transactions.application.id"))


  val builder = new StreamsBuilder

  val transactionsKStream = builder.stream[String, Transaction](conf.getString("transactions.application.source.topic"))
    .mapValues(t => t.maskCreditCard)

  transactionsKStream.to(conf.getString("transactions.application.sink.topic"))

  val streams = new KafkaStreams(builder.build(), props)

  streams.start

  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }

}

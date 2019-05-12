package Serdes

import models.Transaction
import org.apache.kafka.common.serialization.{Serde, Serdes}

class StreamSerde {
  def transactionSerde(): Serde[Transaction] = Serdes.serdeFrom(new KafkaBytesSerializer[Transaction], new KafkaBytesDeserializer[Transaction])

}

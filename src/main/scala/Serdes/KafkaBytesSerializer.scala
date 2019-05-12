package Serdes


import java.io.{ByteArrayOutputStream, ObjectOutputStream}
import java.util

import org.apache.kafka.common.serialization.Serializer

class KafkaBytesSerializer[T] extends Serializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = 0

  override def serialize(topic: String, data: T): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(data)
    oos.close()
    stream.toByteArray
  }

  override def close(): Unit = 0
}
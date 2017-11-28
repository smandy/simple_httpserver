package argoko

import java.net.{InetSocketAddress, SocketAddress, SocketOption, StandardSocketOptions}
import java.nio.ByteBuffer
import java.nio.channels._

object NIOExample {
  def main(args: Array[String]): Unit = {
    val sa = new InetSocketAddress("localhost", 8091)
    val selector = Selector.open()
    val channel = ServerSocketChannel
      .open
      .bind(sa)
    channel.configureBlocking(false)
    channel.register(selector, SelectionKey.OP_ACCEPT)
    //channel.setOption( SocketOption., false)
    //listeningChannel.bind(configuration.bindAddress).configureBlocking(false)
    //listeningChannel.register(selector, SelectionKey.OP_ACCEPT)
    //channel.setOption(StandardSocketOptnions.BLOCKING, false)
    //val connection = channel.accept()
    // val s = connection.get()g
    case class WrappedConnection(channel: SocketChannel) {
      val buffer = ByteBuffer.allocateDirect(1024)
    }
    //var connections = Set[WrappedConnection]()
    while (true) {
      selector.select()
      val quays = selector.selectedKeys()
      //println(s"Quays are $quays")
      quays.forEach(x => {
        if (x.isAcceptable) {
          val sc = channel.accept()
          sc.configureBlocking(false)
          val wc = WrappedConnection(sc)
          sc.register(selector, SelectionKey.OP_READ, wc)
        } else if (x.isReadable) { // Wrapped connnection
          val wc = x.attachment().asInstanceOf[WrappedConnection]
          val bytesRead = wc.channel.read(wc.buffer)
          if (bytesRead == -1) {
            wc.channel.close()
          } else {
            require(bytesRead != 0, "If select for reading should have something")
            wc.buffer.flip()
            //println(s"pos=${b.position()} limit=${b.limit()}")
            val xs = new Array[Byte](wc.buffer.limit() - wc.buffer.position())
            wc.buffer.get(xs, wc.buffer.position(), wc.buffer.limit() - wc.buffer.position())
            val s2 = new String(xs, "UTF-8").trim()
            println(s"got string $s2")
            if (wc.buffer.hasRemaining) {
              wc.buffer.compact()
            } else {
              wc.buffer.clear()
            }
            if (s2 == "quit") {
              wc.channel.close()
              //removeList.add(x)
              // TODO
            }
          }
        }
      }
      )
      quays.clear()
      Thread.sleep(1000)
    }
  }
}

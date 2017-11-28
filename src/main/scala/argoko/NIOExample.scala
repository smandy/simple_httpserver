package argoko
import java.net.{InetSocketAddress, SocketAddress}
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel

object NIOExample {
  def main(args: Array[String]): Unit = {
    val sa = new InetSocketAddress("localhost", 8091)
    val channel = AsynchronousServerSocketChannel.open.bind(sa)
    val connection = channel.accept()
    val s = connection.get()
    println(s"Woot got a connection $s")
    val b = ByteBuffer.allocateDirect(1024)
    var running = true
    while (running) {
      val x = s.read(b)
      val bytesRead = x.get()
      //println(s"Got bytes $bytesRead")
      //println( s"pos=${b.position()} ${b.}
      //println(s"pos=${b.position()} limit=${b.limit()}")
      //println("Flipping")
      b.flip()
      //println(s"pos=${b.position()} limit=${b.limit()}")
      val xs = new Array[Byte](b.limit() - b.position())
      b.get(xs, b.position(), b.limit() - b.position())
      val s2 = new String(xs, "UTF-8").trim()
      println(s"got string $s2")
      if (b.hasRemaining) {
        b.compact()
      } else {
        b.clear()
      }
      if (s2 == "quit") running = false
    }
  }
}

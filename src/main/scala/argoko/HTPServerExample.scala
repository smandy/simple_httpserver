package argoko

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import java.net.InetSocketAddress

object HTTPServerExample {
  def main(args: Array[String]): Unit = {
    val server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/manhattan", new MyHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
  }
}

class MyHandler extends HttpHandler {
  def handle(pl: HttpExchange): Unit = {
    //println("Woohoo " + pl)
    // This guy can chuck us a null if there are no query parameters so
    // we wrap it up nicely with an option.
    val x = Option(pl.getRequestURI().getQuery()).getOrElse("")
    val y = pl.getRequestURI().getPath()
    val bits = {
      val tups = for {
        bits <- x.split("&")
        pair = bits.split("=")
        if (pair.length == 2)
        tup <- pair match {
          case Array(a, b) => Some(a, b)
          case _ => None
        }
      } yield {
        tup
      }
      Map(tups: _*)
    }
    val response = "You sent me " + bits + " in path " + y

    println(response)

    pl.sendResponseHeaders(200, response.length())
    val os = pl.getResponseBody()
    os.write(response.getBytes())
    os.close()
  }
}
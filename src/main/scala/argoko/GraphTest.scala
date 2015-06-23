package argoko


case class Node( nodeId : String, deps : List[String]) {
  import Node._
  nodesById += ( nodeId -> this)

  lazy val dependents : List[Node] = {
    for {
      jobId <- deps
    //_ = println("Lookup " + jobId)
      job <- nodesById.get(jobId)
    } yield {
      job
    }
  }

  private def containsCycle( prev : Set[Node] ) : Boolean = {
    if ( dependents.exists(prev.contains) ) {
      true
    } else {
      val newPrev = prev + this
      dependents.exists( _.containsCycle( newPrev) )
    }
  }

  lazy val isCyclic : Boolean = {
    containsCycle( Set[Node]() )
  }
}

object Node {
  var nodesById = Map[String,Node]()

  def main(args : Array[String]) : Unit = {
    val List(a,b,c) = List(
      ("a",List("b")),
      ("b",List("c")),
      ("c", List("a")) ) map {
      case (a,b) => Node(a, b)
    }

    val List(d,e,f) = List(
      ("d",List("e")),
      ("e",List("f")),
      ("f", List[String]()) ) map {
      case (a,b) => Node(a, b)
    }

    for {
      node <- List(a,b,c,d,e,f)
    } {
      println( "%s isCyclic=%s".format(node.nodeId, node.isCyclic ))
    }
  }
}


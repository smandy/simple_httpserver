package argoko

import scala.math._

object NumericExample {

  implicit object ListNumeric extends Numeric[List[Int]] {

    override def compare(x: List[Int], y: List[Int]): Int = Integer.compare(x.size,y.size)

    override def plus(x: List[Int], y: List[Int]): List[Int] = {
      x ++ y
    }

    override def minus(x: List[Int], y: List[Int]): List[Int] = ???

    override def times(x: List[Int], y: List[Int]): List[Int] = {
      for {
        a <- x
        b <- y
      } yield {
        a * b
      }
    }

    override def negate(x: List[Int]): List[Int] = ???

    override def fromInt(x: Int): List[Int] = ???

    override def toInt(x: List[Int]): Int = ???

    override def toLong(x: List[Int]): Long = ???

    override def toFloat(x: List[Int]): Float = ???

    override def toDouble(x: List[Int]): Double = ???
  }

  def square[T](x : T)(implicit num : Numeric[T]) = {
    import num._
    times(x,x)
  }

  def doAdd[T](x : T)(implicit num : Numeric[T]) = {
    import num._
    plus(x,x)
  }

  def main(args : Array[String]) : Unit = {
    println(square(2))
    println(square(3.0))
    println(doAdd( List(2,3) ))
    println(square( List(2,3) ))
  }
}

package ch2

import org.apache.spark.util.StatCounter

class NAStatCounter extends Serializable {

  val stats: StatCounter = new StatCounter()
  var missing: Long = 0

  // This method mutates state. I'm keeping for consistency with the book,
  // but unless performance considerations arise it is more scala-like to
  // have a method like this return a new instance and make the instances
  // immutable.
  //
  // Note that if we are going to use mutation, it's idiomatic to return
  // a reference to this, allowing so-called 'fluent interfaces'
  def add(x: Double): NAStatCounter = {
    if (x.isNaN()) {
      missing += 1
    } else {
      stats.merge(x)
    }
    this
  }

  def merge(other: NAStatCounter): NAStatCounter = {
    stats.merge(other.stats)
    missing += other.missing
    this
  }

  override def toString = "stats: " + stats.toString + " NaN: " + missing
}

object NAStatCounter extends Serializable {
  def apply(x: Double) = new NAStatCounter().add(x)
}

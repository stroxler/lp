package testingscala

// Remember: an object is a singleton. Here we are using it as if it were a
// Java utility class with a static accumulate method.
object AlbumAccumulator {
  def accumulate(map: Map[String, Int], tuples: Seq[(String, Int)]) = {
    // note that the lambda could have been written just `_ + _`
    tuples.foldLeft(map)((a, b) => a + b)
  }
}

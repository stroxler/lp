package ch2

import org.apache.spark.util.StatCounter
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/* Class that mimics the behavior of Spark's StatCounter, but also
 * filters out NaN's and tracks how many of them we see.
 */
class NaStatCounter extends Serializable {

  val stats: StatCounter = new StatCounter()
  var missing: Long = 0

  def add(x: Double): NaStatCounter = {
    if (x.isNaN()) {
      missing += 1
    } else {
      stats.merge(x)
    }
    this
  }

  def merge(other: NaStatCounter): NaStatCounter = {
    stats.merge(other.stats)
    missing += other.missing
    this
  }

  override def toString = "stats: " + stats.toString + " NaN: " + missing
}

object NaStatCounter extends Serializable {
  def apply() = new NaStatCounter()
}

/* Case class corresponding to one row of the linkage data */
case class MatchData(id1: Int, id2: Int, scores: Array[Double],
                     matches: Boolean)

object DataUtils {

  def loadData(sc: SparkContext, dataPath: String = "_data/block_1.csv") = {

    def isHeader(line: String) = line.contains("id_1")

    def toDoubleWithNaN(num: String) = {
        if (num.equals("?")) Double.NaN else num.toDouble
    }

    def lineToMD(line: String) = {
      val parts = line.split(',')
      val id1 = parts(0).toInt
      val id2 = parts(1).toInt
      val scores = parts.slice(2, 11).map(toDoubleWithNaN)
      val matches = parts(11).toBoolean
      MatchData(id1, id2, scores, matches)
    }

    (sc.textFile(dataPath)
       .filter(!isHeader(_))
       .map(lineToMD))
  }

  def matchCounts(mds: RDD[MatchData]) = {
      (mds.map(_.matches)
          .countByValue()
          .toSeq
          .sortBy(_._2)
          .reverse
          .foreach(println))
  }

  /* Compute an array of NaStatCounter, corresponding to each variable
   * in an RDD of arrays of observations.
   */
  def statsWithMissing(rdd: RDD[Array[Double]]): Array[NaStatCounter] = {

      // Implementation Not:
      //   We use mapPartition here for efficiency: when dealing with mutable
      //   data processing classes like NaStatCounter, you avoid a great deal
      //   of gc overhead by processing in batches and then merging, rather
      //   than processing one element at a time and merging.

      // For mapPartition to work, both the input and output must be an
      // iterator. In our case, we only need to output an iterator of length
      // one ...we aren't converting the array to an iterator, we are wrapping
      // it: the output is Iterator[Array[NaStatCounter]]
      //
      // I had to modify the code in the book to make it run on a smaller
      // dataset, because ins ome cases the partition can be empty after
      // filtering, leading to the next() call raising an exception.
      def computeNaStatsOnPartition(iter: Iterator[Array[Double]]) = {
          val nastats = (0 until 9).map(i => NaStatCounter()).toArray
          iter.foreach(nastats.zip(_).foreach {
              case (nastat, num) => nastat.add(num)
          })
          Iterator(nastats)
      }

      // There are no iterators here; after the mapPartition, everything
      // gets flattened back out by spark.
      def mergePartitions(nastats1: Array[NaStatCounter],
                          nastats2: Array[NaStatCounter]) = {
          nastats1.zip(nastats2) .map { case (ns1, ns2) => ns1.merge(ns2) }
      }


      val nastatsOnPartitions = rdd.mapPartitions(computeNaStatsOnPartition)
      val nastats = nastatsOnPartitions.reduce(mergePartitions)
      nastats
  }
}

object Analysis {

    def apply(sc: SparkContext) = {
        val mds = DataUtils.loadData(sc)
        val ymatches = mds.filter(_.matches)
        val nmatches = mds.filter(!_.matches)
        val ystats = DataUtils.statsWithMissing(ymatches.map(_.scores))
        val nstats = DataUtils.statsWithMissing(nmatches.map(_.scores))
        ystats.zip(nstats).map (
            { case (y, n) => (y.missing+n.missing, y.stats.mean-n.stats.mean) }
        )
    }
}



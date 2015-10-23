package wcex

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.commons.lang.StringUtils

object WC {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Intro"))
    val inputPath = args(0)
    val outputPath = args(1)
    wc(inputPath, outputPath, sc)
  }

  def wc(inputPath: String, outputPath: String, sc: SparkContext): Unit = {
    val rawData = sc.textFile(inputPath)
    val wordCounts = (rawData
      .flatMap(_.split(","))
      .flatMap(_.split(" "))
      .filter(_.length > 0)
      .filter(StringUtils.isAlphanumeric(_))
      .map((_, 1))
      .reduceByKey(_ + _))
    wordCounts.saveAsTextFile(outputPath)
  }

}

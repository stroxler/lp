package ch3

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Ch3 {

}


case class UserArtistData(userId: Int, artistId: Int, playCount: Int)
case class ArtistData(id: Int, name: String)

object LoadUtil {

  def loadData(sc: SparkContext, basePath: String) = {
      val userArtistData = loadUserArtistData(sc, basePath)
      val artistData = loadUserArtistData(sc, basePath)
      val aliasMap = loadAliasesMap(sc, basePath)
      (userArtistData, artistData, aliasMap)
  }

  def loadUserArtistData(sc: SparkContext, basePath: String) = {
    (sc.textFile(basePath + "/user_artist_data.txt")
        .map(_.split(' '))
        .map(row => UserArtistData(row(0).toInt, row(1).toInt, row(2).toInt)))
  }

  def loadArtistData(sc: SparkContext, basePath: String) = {

    def toArtistData(line: String) : Option[ArtistData] = {
        val (id, name) = line.span(_ != '\t')
        if (name.isEmpty) return None
        try {
            return Some(ArtistData(id.toInt, name.trim))
        } catch {
            case e: NumberFormatException => None
        }
    }

    (sc.textFile(basePath + "/artist_data.txt")
       .flatMap(toArtistData))
  }

  def loadAliasesMap(sc: SparkContext, basePath: String) = {
    def readIds(line: String) = {
        val (id1, id2) = line.span(_ != '\t')
        if (id1.isEmpty) None else Some((id1.toInt, id2.toInt))
    }
    (sc.textFile(basePath + "/artist_alias.txt")
       .flatMap(readIds) // reads as Option
       .collectAsMap())
  }

}

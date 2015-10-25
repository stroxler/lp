package ch3

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.recommendation.{Rating,ALS}

object Ch3 {

}


case class Playcount(userId: Int, artistId: Int, playCount: Int)
case class Artist(id: Int, name: String)

object LoadUtil {

  def loadDataWithRatings(sc: SparkContext, basePath: String) = {
      val (playcounts, artists, aliases) = loadData(sc, basePath)

      // converts a Playcount to a Rating by following any aliases
      // Note that in order to use closure well here we needed a broadcast
      val bAliases = sc.broadcast(aliases)
      def playcountToRating(playcount: Playcount) = {
          val origArtistId = playcount.artistId
          val artistId = bAliases.value.getOrElse(origArtistId, origArtistId)
          Rating(playcount.userId, artistId, playcount.playCount)
      }

      val ratings = playcounts.map(playcountToRating)
      (ratings, artists)
  }

  def loadData(sc: SparkContext, basePath: String) = {
      val playcounts = loadPlaycounts(sc, basePath)
      val artists = loadArtists(sc, basePath)
      val aliasMap = loadAliasesMap(sc, basePath)
      (playcounts, artists, aliasMap)
  }

  def loadPlaycounts(sc: SparkContext, basePath: String) = {
    (sc.textFile(basePath + "/user_artist_data.txt")
        .map(_.split(' '))
        .map(row => Playcount(row(0).toInt, row(1).toInt, row(2).toInt)))
  }

  def loadArtists(sc: SparkContext, basePath: String) = {

    def toArtist(line: String) : Option[Artist] = {
        val (id, name) = line.span(_ != '\t')
        if (name.isEmpty) return None
        try {
            return Some(Artist(id.trim.toInt, name.trim))
        } catch {
            case e: NumberFormatException => None
        }
    }

    (sc.textFile(basePath + "/artist_data.txt")
       .flatMap(toArtist))
  }

  def loadAliasesMap(sc: SparkContext, basePath: String) = {
    def readIds(line: String) = {
        val (id1, id2) = line.span(_ != '\t')
        if (id1.isEmpty) None else Some((id1.trim.toInt, id2.trim.toInt))
    }
    (sc.textFile(basePath + "/artist_alias.txt")
       .flatMap(readIds) // reads as Option
       .collectAsMap())
  }

}

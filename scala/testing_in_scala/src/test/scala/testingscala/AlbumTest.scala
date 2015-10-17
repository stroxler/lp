package testingscala

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class AlbumTest extends FunSpec with ShouldMatchers {
    describe("An Album") {
        it ("can add an Artist object to the album") {
            val album = new Album("Thriller", 1981,
                                  new Artist("Michael", "Jackson"))
        }
    }
}

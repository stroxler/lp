package testingscala

import org.scalatest.{FunSpec, Tag, GivenWhenThen}
import org.scalatest.matchers.ShouldMatchers

class AlbumTest extends FunSpec with ShouldMatchers with GivenWhenThen {

    describe("An Album") {

        // An extremely simple spec
        it ("can set the album title") {
            val album = new Album("Thriller", 1981,
                                  new Artist("Michael", "Jackson"))
            album.title should be ("Thriller")
        }

        // A spec illustrating more features, particularly GivenWhenThen
        // but also tags, which give testing metadata.
        it("can add an Artist to the Album at contruction time",
          Tag("construction")) {

            given("The album Thriller by Michael Jackson")
            val album = new Album("Thriller", 1981,
                                  new Artist("Michael", "Jackson"))
            
            when("The first act of the album is obtained")
            val act = album.acts.head

            then("The act should be an instance of Artist")
            act.isInstanceOf[Artist] should be (true)

            and("The artist's name should be Michael Jackson")
            val artist = act.asInstanceOf[Artist]
            artist.firstName should be ("Michael")
            artist.lastName should be ("Jackson")
        }

        // This is how you mark a test as incomplete
        // It's also how you create messages which print at test runtime
        it("can do some other stuff...") {
          info("This is how you'd say what you want to test but haven't done yet")
          pending
        }

        // By changing it to ignore, you can bypass a test completely
        ignore("Some tests just shouldn't be run, I guess?") {
        }

    } 

    
}

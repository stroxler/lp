package testingscala

import org.scalatest.{FunSpec, Spec}
import org.scalatest.matchers.ShouldMatchers

/* This test isn't of production code at all, it's a demo of scalatest
 * behavior.
 */

class ShouldMatcherSpec extends FunSpec with ShouldMatchers {

  describe("Using all should matchers") {
    it("has simple matchers") {
      val list = 2 :: 4 :: 5 :: Nil
      list.size should be(3)
      list.size should equal(3)
    }

    it("has string matchers") {
      val string = """I fell into a burning ring of fire.
          I went down, down, down and the flames went higher"""
      // plain string matches
      string should startWith("I fell")
      string should endWith("higher")
      string should not endWith "the end"
      string should include("down, down, down")
      string should not include("Great balls of fire")
      // regex matches
      string should startWith regex ("I.fel+")
      string should endWith regex ("h.{4}r")
      string should not endWith regex ("\\d{5}")
      string should include regex ("flames?")
      string should fullyMatch regex("""I(.|\n|\S)*higher""")
    }

    it("has <, >, <=, >=, === matchers") {
      val answerToLife = 42
      answerToLife should be < (50)
      answerToLife should not be > (50)
      answerToLife should be > (3)
      answerToLife should be <= (100)
      answerToLife should be >= (0)
      answerToLife should be === (42)
      answerToLife should not be === (400)
    }

    it("has checking floating point imperfections") {
      (4.0 + 1.2) should be(5.2)
      (0.9 - 0.8) should be (0.1 plusOrMinus .001)
      (0.4 + 0.1) should not be (1.0 plusOrMinus 0.02)
    }

    it("has methods for iterable") {
      List() should be ('empty)
      8 :: 6 :: 7 :: 3 :: Nil should contain(7)
    }

    it("has methods for seq") {
      (1 to 9).toList should have length(9)
    }

    it("has methods for traversable") {
      (20 to 60 by 2).toList should have size (21)
    }

    it("has methods for map") {
      val map = Map(
        "Jimmy Page" -> "Led Zepplin",
        "Sting" -> "The Police"
        )
      map should contain key ("Sting")
      map should contain value ("Led Zepplin")
      map should not contain key ("Muddy Waters")
    }

    // TODO java collections, compound and and or
    it("has method for java collections") {
      import java.util.{
        List => JList, ArrayList => JArrayList, Map => JMap, HashMap => JHashMap
      }
      val jList: JList[Int] = new JArrayList[Int](20)
      jList.add(3)
      jList.add(6)
      jList.add(9)
      val emptyJList: JList[Int] = new JArrayList[Int]()
      emptyJList should be('empty)
      jList should have length(3)
      jList should have size(3)
      jList should contain(6)
      jList should not contain(7)

      val backupBands: JMap[String, String] = new JHashMap()
      backupBands.put("Joan Jett", "Blackhearts")
      backupBands.put("Tom Petty", "Heartbreakers")
      
      backupBands should contain key ("Joan Jett")
      backupBands should contain value ("Heartbreakers")
      backupBands should not contain key ("Bruce Springstein")
    }

    it ("has compound and and or") {
      var redHotChiliPeppers = List("Anthony Kiedis", "Flea", "Chad Smith",
        "Josh Klinghoffer")
      redHotChiliPeppers should (
        contain("Anthony Kiedis")
        and (not (contain("John Coltrane") or contain ("Miles Davis")))
      )
      redHotChiliPeppers should not (contain ("Me") or contain ("You"))

      // what's the point of this?
      // the or actually does execute, which we know because total is 9 after
      // and, the value passed to contain is the value of the whole expression,
      // which is the value of the last subexpression... "Kenny G"
      var total = 3
      redHotChiliPeppers should not (contain("Sting") or contain {total += 6; "Kenny G"})
      total should be (9)

    }

    it ("has the ability to check properties") {
      import scala.collection.mutable.WrappedArray
      val album = new Album("Blizzard of Ozz", 1980,
        new Artist("Ozzy", "Osbourne"))
      album should have (
        'title ("Blizzard of Ozz"),
        'year (1980),
        'acts (new WrappedArray.ofRef(List(new Artist("Ozzy", "Osbourne")).toArray))
      )
    }
  }
}

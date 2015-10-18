package testingscala

import org.scalatest.{FunSpec, Spec}
import org.scalatest.matchers.MustMatchers

/* This test isn't of production code at all, it's a demo of scalatest
 * behavior.
 */

class MustMatcherSpec extends FunSpec with MustMatchers {

  describe("Using all must matchers") {
    it("has simple matchers") {
      val list = 2 :: 4 :: 5 :: Nil
      list.size must be(3)
      list.size must equal(3)
    }

    it("has string matchers") {
      val string = """I fell into a burning ring of fire.
          I went down, down, down and the flames went higher"""
      // plain string matches
      string must startWith("I fell")
      string must endWith("higher")
      string must not endWith "the end"
      string must include("down, down, down")
      string must not include("Great balls of fire")
      // regex matches
      string must startWith regex ("I.fel+")
      string must endWith regex ("h.{4}r")
      string must not endWith regex ("\\d{5}")
      string must include regex ("flames?")
      string must fullyMatch regex("""I(.|\n|\S)*higher""")
    }

    it("has <, >, <=, >=, === matchers") {
      val answerToLife = 42
      answerToLife must be < (50)
      answerToLife must not be > (50)
      answerToLife must be > (3)
      answerToLife must be <= (100)
      answerToLife must be >= (0)
      answerToLife must be === (42)
      answerToLife must not be === (400)
    }

    it("has checking floating point imperfections") {
      (4.0 + 1.2) must be(5.2)
      (0.9 - 0.8) must be (0.1 plusOrMinus .001)
      (0.4 + 0.1) must not be (1.0 plusOrMinus 0.02)
    }

    it("has methods for iterable") {
      List() must be ('empty)
      8 :: 6 :: 7 :: 3 :: Nil must contain(7)
    }

    it("has methods for seq") {
      (1 to 9).toList must have length(9)
    }

    it("has methods for traversable") {
      (20 to 60 by 2).toList must have size (21)
    }

    it("has methods for map") {
      val map = Map(
        "Jimmy Page" -> "Led Zepplin",
        "Sting" -> "The Police"
        )
      map must contain key ("Sting")
      map must contain value ("Led Zepplin")
      map must not contain key ("Muddy Waters")
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
      emptyJList must be('empty)
      jList must have length(3)
      jList must have size(3)
      jList must contain(6)
      jList must not contain(7)

      val backupBands: JMap[String, String] = new JHashMap()
      backupBands.put("Joan Jett", "Blackhearts")
      backupBands.put("Tom Petty", "Heartbreakers")
      
      backupBands must contain key ("Joan Jett")
      backupBands must contain value ("Heartbreakers")
      backupBands must not contain key ("Bruce Springstein")
    }

    it ("has compound and and or") {
      var redHotChiliPeppers = List("Anthony Kiedis", "Flea", "Chad Smith",
        "Josh Klinghoffer")
      redHotChiliPeppers must (
        contain("Anthony Kiedis")
        and (not (contain("John Coltrane") or contain ("Miles Davis")))
      )
      redHotChiliPeppers must not (contain ("Me") or contain ("You"))

      // what's the point of this?
      // the or actually does execute, which we know because total is 9 after
      // and, the value passed to contain is the value of the whole expression,
      // which is the value of the last subexpression... "Kenny G"
      var total = 3
      redHotChiliPeppers must not (contain("Sting") or contain {total += 6; "Kenny G"})
      total must be (9)

    }

    it ("has the ability to check properties") {
      import scala.collection.mutable.WrappedArray
      val album = new Album("Blizzard of Ozz", 1980,
        new Artist("Ozzy", "Osbourne"))
      album must have (
        'title ("Blizzard of Ozz"),
        'year (1980),
        'acts (new WrappedArray.ofRef(List(new Artist("Ozzy", "Osbourne")).toArray))
      )
    }
  }
}

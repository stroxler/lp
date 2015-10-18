package testingscala

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec

import org.joda.time.Period

class TrackFlatSpec extends FunSpec with ShouldMatchers {

  describe("A Track") {

    it ("""should have a constructor that accepts the name of the track and the 
          length in min:sec""") {
      val track = new Track("Last Dance", "5:00")
      track.period should be (new Period(0, 5, 0, 0))
    }

    it ("""should throw an IllegalArgumentException with the message "Track name
          cannot be blank" when the name of the track is blank""") {
      val exception = evaluating(new Track("")) should produce [IllegalArgumentException]
      exception.getMessage should be ("requirement failed: Track name cannot be blank")
    }
  }

}

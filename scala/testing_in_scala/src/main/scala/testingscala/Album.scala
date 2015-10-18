package testingscala

import org.joda.time.Period

class Album(val title: String, val year: Int, val tracks: Option[List[Track]],
            val acts: Act*) {
    require(acts.size > 0)

    // Note: None is a special value for Options, and :_* is the unpack operator
    def this(title: String, year:Int, acts: Act*) = this(title, year, None, acts:_*)

    def ageFrom(now: Int) = now - year

    // The _ and _ are automatically the first and second args of a lambda.
    def period = tracks.getOrElse(Nil).map(_.period).foldLeft(Period.ZERO)(_.plus(_))

    // A level of parentheses without a comma has no effect (as in python)
    override def toString = ("Album[" + title + "]")
}

package testingscala

// this syntax says the the JukeBox main constructor is private
// clients can only instantiate using the constructor on line 6.
// when we play a jukebox, notice that it creates a new JukeBox rather than
// mutating an existing one.
final class JukeBox private (val albums: Option[List[Album]], val currentTrack: Option[Track]) {
  def this(albums: Option[List[Album]]) = this(albums, None)
  def readyToPlay = albums.isDefined
  def play = new JukeBox(albums, Some(albums.get(0).tracks.get(0)))
}

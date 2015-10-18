package testingscala

import org.scalatest.junit.JUnitSuite
import org.junit.{Test, Before, After}
// notice that in scala we don't need a separate syntax for 'import static'
import org.junit.Assert.{assertEquals}

class ArtistJUnitSuite extends JUnitSuite {

  // I'm not 100% sure what this syntax means. Probably artist is null.
  var artist: Artist = _

  @Before
  def startSuite() {
    artist = new Artist("Kenny", "Rogers")
  }

  @Test
  def addOneAlbumAndGetCopy() {
    val album = new Album("Love will turn you around", 1982, artist)
    val copyArtist = artist.addAlbum(album)
    assertEquals(copyArtist.albums.size, 1)
  }

  @Test
  def addTwoAlbumsAndGetCopy() {
    val album0 = new Album("Love will turn you around", 1982, artist)
    val album1 = new Album("We've got tonight", 1983, artist)
    val copyArtist = artist.addAlbum(album0).addAlbum(album1)
    assertEquals(copyArtist.albums.size, 2)
  }

  @After
  def endSuite() {
    artist = null
  }
}

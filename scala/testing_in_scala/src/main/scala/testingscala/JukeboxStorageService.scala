package testingscala

class JukeboxStorageService(dao: DAO) {

  def persist(jukebox: JukeBox) {
    jukebox.albums.getOrElse(Nil).foreach {
      album => dao.persist(album)
      album.acts.foreach(act => dao.persist(act))
    }
  }
}

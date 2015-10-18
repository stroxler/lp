package testingscala

class AlbumMultipleStorageService {

  // these are instance fields which are *not* constructor arguments.
  val mysqlDAO = DAO.createMySqlDAO
  val db2DAO = DAO.createDB2DAO

  // Recall that this syntax, with no `=`, is equivalent to
  // def persist(...) = Unit { ... }, which is scala's form for void functions
  def persist(album: Album) {
    mysqlDAO.persist(album)
    db2DAO.persist(album)
    album.acts.foreach{ act => mysqlDAO.persist(act); db2DAO.persist(act) }
  }
}

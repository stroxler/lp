package testingscala

/* As of this writing, I do not understand this code at all. I need to read
 * about scala traits. It looks like it might be somewhat akin to a Java
 * interface
 */
trait DAO {
  def persist[T](t: T)
}

object DAO {

  private class MySqlDAO extends DAO {
    // this is generic syntax in scala
    // this is a void method that does nothing, so it's not very interesting...
    def persist[T](t: T) { println("in MySqlDAO, persisting %s".format(t)) }
  }

  private class DB2DAO extends DAO {
    def persist[T](t: T) { println("in DB2DAO, persisting %s".format(t)) }
  }

  def createMySqlDAO: DAO = new MySqlDAO

  def createDB2DAO: DAO = new DB2DAO
}

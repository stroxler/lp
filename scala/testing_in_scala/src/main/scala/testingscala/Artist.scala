package testingscala

class Artist(val firstName: String, val lastName: String) extends Act {

  override def equals(other: Any) = other match {
    case that: Artist => this.firstName == that.firstName &&
      this.lastName == that.lastName
    case _ => false
  }
  override def toString = "Artist[%s, %s]".format(firstName, lastName)
}

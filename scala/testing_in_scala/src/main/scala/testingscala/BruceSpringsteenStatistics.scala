package testingscala

object BruceSpringsteenStatistics {
  
  def numberOfAlbums = BruceSpringsteenFactory.discography.size

  // note that numberOfAlbums is a function but it's called without ()
  // in scala this is considered idiomatic for functionally pure no-arg methods,
  // but methods with side effects should be called using ()
  def averageYear = BruceSpringsteenFactory.discography.map(_.year).sum / numberOfAlbums

}

package testingscala

import org.scalatest.testng.TestNGSuite
import collection.mutable.ArrayBuilder
import org.testng.annotations.{Test, DataProvider}
// no need for import static
import org.testng.Assert._

class ArtistTestNGSuite extends TestNGSuite {

  @DataProvider(name = "provider")
  def profideData = {
    val g = new ArrayBuilder.ofRef[Array[Object]]
    g += (Array[Object]("Heart", 5.asInstanceOf[java.lang.Integer]))
    g += (Array[Object]("Jimmy Buffet", 12.asInstanceOf[java.lang.Integer]))
    g.result()
  }

  @Test(dataProvider = "provider", groups=Array("word_count_analysis"))
  def testTheStringLength(n1: String, n2: java.lang.Integer) {
    assertEquals(n1.length, n2)
  }
}

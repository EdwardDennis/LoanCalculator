package services

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import models.loan.Loan

class DataServiceImplSpec extends AnyWordSpec with Matchers {

  "DataServiceImpl" when {
    "adding elements" should {
      "correctly add them to the store" in {
        val dataService = new DataServiceImpl[Int]
        dataService.add(1)
        dataService.add(2)
        dataService.getAll should equal(List(0 -> 1, 1 -> 2))
      }
    }

    "getting all elements" should {
      "return all elements in the store in the order they were added" in {
        val dataService = new DataServiceImpl[String]
        dataService.add("first")
        dataService.add("second")
        dataService.getAll should equal(List(0 -> "first", 1 -> "second"))
      }

      "return an empty list when no elements are in the store" in {
        val dataService = new DataServiceImpl[Loan]
        dataService.getAll shouldBe empty
      }
    }
  }
}


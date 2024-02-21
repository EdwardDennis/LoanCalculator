package services

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import models.loan.Loan
import java.time.LocalDate

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

    "editing an element" should {
      val startDate: LocalDate = LocalDate.parse("2022-01-01")
      val endDate: LocalDate = LocalDate.parse("2023-01-31")
      "successfully update when item exists in the store" in {
        val dataService = new DataServiceImpl[Loan]
        val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))

        dataService.add(loan)

        val updatedLoan = loan.copy(amount = BigDecimal(2000))

        dataService.edit(0, updatedLoan) shouldBe true
        dataService.getAll should equal(List(0 -> updatedLoan))
      }

      "return false when the item does not exist in the store" in {
        val dataService = new DataServiceImpl[Loan]

        val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))

        dataService.edit(0, loan) shouldBe false
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


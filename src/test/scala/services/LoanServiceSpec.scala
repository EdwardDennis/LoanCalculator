package services

import models.loan.Loan
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDate

class LoanServiceSpec extends AnyWordSpec with Matchers {
  "LoanService" when {
    "getAllLoans" should {
      "return all the loans" in {
        val dataServiceMock = new DataServiceImpl[Loan]()
        val loanService = new LoanService(dataServiceMock)

        val startDate: LocalDate = LocalDate.parse("2022-01-01")
        val endDate: LocalDate = LocalDate.parse("2022-01-31")
        val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))

        dataServiceMock.add(loan)

        val allLoans = loanService.getAllLoans

        allLoans should have size 1
        allLoans.head shouldBe (0 -> loan)
      }
    }
  }
}


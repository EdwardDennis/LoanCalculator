package models.loan

import models.loan.Loan
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDate

class LoanSpec extends AnyWordSpec with Matchers {

  val startDate: LocalDate = LocalDate.parse("2022-01-01")
  val endDate: LocalDate = LocalDate.parse("2022-01-31")
  val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))

  "A Loan" should {
    "calculate total interest rate correctly" in {
      loan.totalInterestRate shouldBe 7
    }

    "calculate total interest with margin correctly" in {
      loan.totalInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(5.95)
    }

    "calculate total interest without margin correctly" in {
      loan.totalInterestWithoutMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(4.25)
    }

    "calculate days elapsed since start date correctly" in {
      loan.daysElapsedSinceStartDate() shouldBe 31
    }

    "calculate daily interest with margin correctly" in {
      loan.dailyInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(0.19)
    }

    "calculate daily interest without margin correctly" in {
      loan.dailyInterestWithoutMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(0.14)
    }
  }
}


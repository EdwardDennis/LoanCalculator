package models.loan

import java.time.LocalDate
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester.*
import models.loan.Loan

class LoanSpec extends AnyWordSpec with Matchers {

  val startDate: LocalDate = LocalDate.parse("2022-01-01")
  val endDate: LocalDate = LocalDate.parse("2022-01-31")

  "A Loan" should {
    "calculate total interest rate correctly" in {
      val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))
      loan.totalInterestRate shouldBe 7
    }

    "calculate total interest with margin correctly" in {
      val loan = Loan(startDate, endDate, BigDecimal(100), "USD", BigDecimal(5), BigDecimal(2))
      loan.totalInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(0.59)
    }

    "calculate total interest without margin correctly" in {
      val loan = Loan(startDate, endDate, BigDecimal(100), "USD", BigDecimal(5), BigDecimal(2))
      loan.totalInterestWithoutMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN) shouldBe BigDecimal(0.42)
    }
  }

  "A DailyInterest" should {
    "calculate numberOfDaysSinceStart correctly" in {
      val loan = Loan(startDate, endDate, BigDecimal(1000), "USD", BigDecimal(5), BigDecimal(2))

      val calculateDailyInterest = PrivateMethod[List[DailyInterest]](Symbol("calculateDailyInterest"))
      val dailyInterestList = loan invokePrivate calculateDailyInterest()

      dailyInterestList.last.numberOfDaysSinceStart shouldBe 30
    }
  }
}


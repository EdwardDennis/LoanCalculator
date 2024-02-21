package services

import models.loan.Loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class LoanService(dataServiceImpl: DataServiceImpl[Loan]) {

  def getAllLoans: List[String] = {
    dataServiceImpl.getAll.map { case (id, loan) =>
      loanToString(id, loan)
    }
  }

  private def loanToString(id: Int, loan: Loan): String = {
    val dailyData = for {
      day <- 0 until DAYS.between(loan.startDate, loan.endDate.plusDays(1)).toInt
    } yield {
      val currentDate = loan.startDate.plusDays(day)
      val elapsedDays = day + 1
      val interestWithoutMargin = loan.dailyInterestWithoutMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
      val interestWithMargin = loan.dailyInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
      f"Day: $currentDate, Elapsed Days: $elapsedDays, Daily Interest (Without Margin): $interestWithoutMargin ${loan.currency}, Daily Interest (With Margin): $interestWithMargin ${loan.currency}"
    }
    val totalInterest = loan.totalInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
    s"""
       |ID: $id
       |Accrual Start Date: ${loan.startDate}
       |-------------------------
       |${dailyData.mkString("\n")}
       |-------------------------
       |Total Interest With Margin: $totalInterest
""".stripMargin
  }
}

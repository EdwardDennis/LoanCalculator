package models.loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

case class DailyInterest(accrualDate: LocalDate, interestWithoutMargin: BigDecimal, interestWithMargin: BigDecimal, numberOfDaysSinceStart: Long)

case class Loan(startDate: LocalDate, endDate: LocalDate, amount: BigDecimal,
                currency: String, baseInterestRate: BigDecimal, margin: BigDecimal) {

  private def calculateDailyInterest(): List[DailyInterest] = {
    val totalDays = DAYS.between(startDate, endDate).toInt
    (0 to totalDays).toList.map { i =>
      val accrualDate = startDate.plusDays(i)
      val interestWithoutMargin = amount * (baseInterestRate / 100) / 365
      val interestWithMargin = amount * ((baseInterestRate + margin) / 100) / 365
      DailyInterest(accrualDate, interestWithoutMargin, interestWithMargin, i)
    }
  }

  def totalInterestRate: BigDecimal = baseInterestRate + margin

  def totalInterestWithMargin(): BigDecimal = {
    calculateDailyInterest().map(_.interestWithMargin).sum
  }

  def totalInterestWithoutMargin(): BigDecimal = {
    calculateDailyInterest().map(_.interestWithoutMargin).sum
  }

  override def toString: String = {
    s"Start Date: $startDate, End Date: $endDate, Amount: $amount $currency, Base Interest Rate: $baseInterestRate%, Margin: $margin%"
  }
}

object Loan {
  def fromUserInput(startDate: LocalDate,
                    endDate: LocalDate,
                    amount: BigDecimal,
                    currency: String,
                    baseInterestRate: BigDecimal,
                    margin: BigDecimal): Loan = {
    Loan(startDate, endDate, amount, currency, baseInterestRate, margin)
  }
}

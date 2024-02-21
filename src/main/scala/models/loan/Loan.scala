package models.loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

case class Loan(startDate: LocalDate, endDate: LocalDate, amount: BigDecimal,
                currency: String, baseInterestRate: BigDecimal, margin: BigDecimal) {

  def daysElapsedSinceStartDate(): Int = DAYS.between(startDate, endDate.plusDays(1)).toInt

  def dailyInterestWithMargin(): BigDecimal = amount * ((baseInterestRate + margin) / 100) / 365
  def dailyInterestWithoutMargin(): BigDecimal = amount * (baseInterestRate / 100) / 365

  def totalInterestRate: BigDecimal = baseInterestRate + margin

  def totalInterestWithMargin(): BigDecimal = {
    daysElapsedSinceStartDate() * dailyInterestWithMargin()
  }

  def totalInterestWithoutMargin(): BigDecimal = {
    daysElapsedSinceStartDate() * dailyInterestWithoutMargin()
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

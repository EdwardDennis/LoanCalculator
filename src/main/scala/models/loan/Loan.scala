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
    val dailyData = for {
      day <- 0 until DAYS.between(startDate, endDate.plusDays(1)).toInt
    } yield {
      val currentDate = startDate.plusDays(day)
      val elapsedDays = day + 1
      val interestWithoutMargin = dailyInterestWithoutMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
      val interestWithMargin = dailyInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
      f"Day: $currentDate, Elapsed Days: $elapsedDays, Daily Interest (Without Margin): $interestWithoutMargin $currency, Daily Interest (With Margin): $interestWithMargin $currency"
    }
    val totalInterest = totalInterestWithMargin().setScale(2, BigDecimal.RoundingMode.HALF_EVEN)
    s"""
       |Accrual Start Date: $startDate
       |-------------------------
       |${dailyData.mkString("\n")}
       |-------------------------
       |Total Interest With Margin: $totalInterest
 """.stripMargin
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

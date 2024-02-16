package controllers

import models.loan.Loan

import java.time.LocalDate
import java.util.Currency
import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

class LoanController {
  def createLoan(): Unit = {
    val startDate = getStartDate
    val endDate = getEndDate
    val amount = getAmount
    val currency = getCurrency
    val baseInterestRate = getBaseInterestRate
    val margin = getMargin
    val loan = Loan(startDate, endDate, amount, currency.getCurrencyCode, baseInterestRate, margin)
    println(s"Loan successfully created: ${loan.toString}")
  }

  @tailrec
  private final def getStartDate: LocalDate = {
    println("Please enter the loan start date (format: yyyy-mm-dd): ")
    val startDate = readLine()
    Try(LocalDate.parse(startDate)) match {
      case Success(startDate) => startDate
      case Failure(exception) =>
        println("Invalid date format. Please try again.")
        getStartDate
    }
  }

  @tailrec
  private final def getEndDate: LocalDate = {
    println("Please enter the loan end date (format: yyyy-mm-dd): ")
    val endDate = readLine()
    Try(LocalDate.parse(endDate)) match {
      case Success(endDate) => endDate
      case Failure(exception) =>
        println("Invalid date format. Please try again.")
        getEndDate
    }
  }

  @tailrec
  private final def getAmount: BigDecimal = {
    println("Loan amount: ")
    val amount = readLine()
    Try(BigDecimal(amount)) match {
      case Success(amount) => amount
      case Failure(exception) =>
        println("Invalid amount. Please try again.")
        getAmount
    }
  }

  @tailrec
  private final def getCurrency: Currency = {
    println("Currency: ")
    val currency = readLine().toUpperCase
    Try(Currency.getInstance(currency)) match {
      case Success(currency) => currency
      case Failure(exception) =>
        println("Invalid currency code. Please try again.")
        getCurrency
    }
  }

  @tailrec
  private final def getBaseInterestRate: BigDecimal = {
    println("Base Interest Rate: ")
    val baseInterestRate = readLine()

    Try(BigDecimal(baseInterestRate)) match {
      case Success(baseInterestRate) => baseInterestRate
      case Failure(exception) =>
        println("Invalid base interest amount. Please enter a valid number.")
        getBaseInterestRate
    }
  }

  @tailrec
  private final def getMargin: BigDecimal = {
    println("Margin: ")
    val margin = readLine()

    Try(BigDecimal(margin)) match {
      case Success(margin) => margin
      case Failure(exception) =>
        println("Invalid margin. Please enter a valid number.")
        getMargin
    }
  }
}

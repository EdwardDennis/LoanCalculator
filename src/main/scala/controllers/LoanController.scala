package controllers

import models.loan.Loan

import java.time.LocalDate
import java.util.Currency
import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}
import java.io.{BufferedReader, InputStreamReader, PrintStream}

class LoanController(in: BufferedReader = new BufferedReader(new InputStreamReader(System.in)),
                     out: PrintStream = System.out) {
  def createLoan(): Unit = {
    val startDate = getStartDate
    val endDate = getEndDate(startDate)
    val amount = getAmount
    val currency = getCurrency
    val baseInterestRate = getBaseInterestRate
    val margin = getMargin
    val loan = Loan(startDate, endDate, amount, currency.getCurrencyCode, baseInterestRate, margin)
    println(s"Loan successfully created: ${loan.toString}")
  }

  private final def getStartDate: LocalDate = {
    readWithRetry("Please enter the loan start date (format: yyyy-mm-dd): ", "Invalid start date format. Please try again.", str => Try(LocalDate.parse(str)).toEither)
  }
  private final def getEndDate(startDate: LocalDate): LocalDate = {
    val prompt = "Please enter the loan end date (format: yyyy-mm-dd): "
    val parseErrorMsg = "Invalid end date format. Please try again."
    val comparisonErrorMsg = "End date must be after the start date. Please try again."

    val parsedDate = readWithRetry(prompt, parseErrorMsg, str => Try(LocalDate.parse(str)).toEither)

    if (parsedDate.isAfter(startDate)) parsedDate
    else {
      out.println(comparisonErrorMsg)
      readWithRetry("Please enter the loan start date (format: yyyy-mm-dd): ", comparisonErrorMsg, str => Try(LocalDate.parse(str)).toEither)
    }
  }

  private final def getAmount: BigDecimal = {
    readWithRetry("Loan amount: ", "Invalid amount. Please try again.", str => Try(BigDecimal(str)).toEither)
  }

  private final def getCurrency: Currency = {
    readWithRetry("Currency: ", "Invalid currency code. Please try again.", str => Try(Currency.getInstance(str)).toEither)
  }

  private final def getBaseInterestRate: BigDecimal = {
    readWithRetry("Base Interest Rate: ", "Invalid base interest amount. Please enter a valid number.", str => Try(BigDecimal(str)).toEither)
  }

  private final def getMargin: BigDecimal = {
    readWithRetry("Margin: ", "Invalid margin. Please enter a valid number.", str => Try(BigDecimal(str)).toEither)
  }

  @tailrec
  private def readWithRetry[T](prompt: String, errorMsg: String, validateFn: String => Either[Throwable, T]): T = {
    out.print(prompt)
    val input = in.readLine().trim

    validateFn(input) match {
      case Right(value) => value
      case Left(_) =>
        out.println(errorMsg)
        readWithRetry(prompt, errorMsg, validateFn)
    }
  }
}

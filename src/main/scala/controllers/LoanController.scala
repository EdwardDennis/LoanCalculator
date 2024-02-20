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
    val endDate = getEndDate
    val amount = getAmount
    val currency = getCurrency
    val baseInterestRate = getBaseInterestRate
    val margin = getMargin
    val loan = Loan(startDate, endDate, amount, currency.getCurrencyCode, baseInterestRate, margin)
    println(s"Loan successfully created: ${loan.toString}")
  }

  private final def getStartDate: LocalDate = readWithRetry("Please enter the loan start date (format: yyyy-mm-dd): ", "Invalid start date format. Please try again.", LocalDate.parse)

  private final def getEndDate: LocalDate = readWithRetry("Please enter the loan end date (format: yyyy-mm-dd): ", "Invalid end date format. Please try again.", LocalDate.parse)

  private final def getAmount: BigDecimal = readWithRetry("Loan amount: ", "Invalid amount. Please try again.", BigDecimal(_))

  private final def getCurrency: Currency = readWithRetry("Currency: ", "Invalid currency code. Please try again.", Currency.getInstance)

  private final def getBaseInterestRate: BigDecimal = readWithRetry("Base Interest Rate: ", "Invalid base interest amount. Please enter a valid number.", BigDecimal(_))

  private final def getMargin: BigDecimal = readWithRetry("Margin: ", "Invalid margin. Please enter a valid number.", BigDecimal(_))

  @tailrec
  private def readWithRetry[T](prompt: String, errorMsg: String, parseFn: String => T): T = {
    out.print(prompt)
    val input = in.readLine().trim

    Try(parseFn(input)) match {
      case Success(value) => value
      case Failure(_) =>
        out.println(errorMsg)
        readWithRetry(prompt, errorMsg, parseFn)
    }
  }
}

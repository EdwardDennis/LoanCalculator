package controllers

import models.loan.Loan

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.Currency
import scala.annotation.tailrec
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

class LoanController(in: BufferedReader = new BufferedReader(new InputStreamReader(System.in)),
                     out: PrintStream = System.out) {

  @tailrec
  final def askUserForAction(): Unit = {
    out.println("Available commands: ")
    out.println("- 'c' ,'calc', or 'calculate': perform a new loan calculation.")
    out.print("What do you want to do next? ")

    val input = in.readLine().toLowerCase().trim

    input match {
      case "c" | "calc" | "calculate" => handleNewLoan()
      case _ =>
        out.println("Unsupported action. Please try again.")
        askUserForAction()
    }
  }


  private final def handleNewLoan(): Unit = {
    val maybeLoan = getLoanDetailsFromUser
    maybeLoan match {
      case Right(loan) => println(s"Loan successfully created: $loan")
      case Left(ex) =>
        println(ex.getMessage)
        askUserForAction()
    }
  }

  private final def getLoanDetailsFromUser: Either[Throwable, Loan] = {
    getStartDate match {
      case Right(startDate) =>
        for {
          endDate <- getEndDate(startDate)
          amount <- getAmount
          currency <- getCurrency
          baseInterestRate <- getBaseInterestRate
          margin <- getMargin
        } yield Loan(startDate, endDate, amount, currency.getCurrencyCode, baseInterestRate, margin)
      case Left(ex) => Left(ex)
    }
  }

  private final def getStartDate: Either[Throwable, LocalDate] = {
    getUserInput("Please enter the loan start date (format: yyyy-mm-dd): ", "Invalid start date format. Please try again.", str => Try(LocalDate.parse(str)).toEither)
  }

  private final def getEndDate(startDate: LocalDate): Either[Throwable, LocalDate] = {
    val userInput = getUserInput("Please enter the loan end date (format: yyyy-mm-dd): ", "Invalid end date format. Please try again.", str => Try(LocalDate.parse(str)).toEither)
    userInput match {
      case Right(endDate) if endDate.isAfter(startDate) => Right(endDate)
      case _ => Left(new IllegalArgumentException("End date must be after the start date."))
    }
  }

  private final def getAmount: Either[Throwable, BigDecimal] = {
    getUserInput("Loan amount: ", "Invalid amount. Please try again.", str => Try(BigDecimal(str)).toEither)
  }

  private final def getCurrency: Either[Throwable, Currency] = {
    getUserInput("Currency: ", "Invalid currency code. Please try again.", str => Try(Currency.getInstance(str)).toEither)
  }

  private final def getBaseInterestRate: Either[Throwable, BigDecimal] = {
    getUserInput("Base Interest Rate: ", "Invalid base interest amount. Please enter a valid number.", str => Try(BigDecimal(str)).toEither)
  }

  private final def getMargin: Either[Throwable, BigDecimal] = {
    getUserInput("Margin: ", "Invalid margin. Please enter a valid number.", str => Try(BigDecimal(str)).toEither)
  }

  private def getUserInput[T](prompt: String, errorMsg: String, validateFn: String => Either[Throwable, T]): Either[Throwable, T] = {
    out.print(prompt)
    val input = in.readLine().toUpperCase.trim

    validateFn(input) match {
      case result@Right(_) => result
      case Left(_) => Left(IllegalArgumentException(errorMsg))
    }
  }
}

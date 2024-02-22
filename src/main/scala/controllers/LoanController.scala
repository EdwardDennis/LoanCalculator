package controllers

import models.loan.Loan
import services.LoanService

import java.io.{BufferedReader, InputStreamReader, PrintStream}
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.Currency
import scala.io.StdIn.readLine
import scala.util.Try

class LoanController(in: BufferedReader = new BufferedReader(new InputStreamReader(System.in)),
                     out: PrintStream = System.out,
                     loanService: LoanService) {

  def askUserForAction(): Unit = {
    out.println("Available commands: ")
    out.println("- 'c', 'calc', or 'calculate': perform a new loan calculation.")
    out.println("- 'v', or 'view': view previous calculations.")
    out.println("- 'x', or 'exit': close the application.")
    out.println("What do you want to do next? ")

    LazyList.continually(in.readLine().toLowerCase().trim).foreach {
      case "c" | "calc" | "calculate" => handleNewLoan()
      case "v" | "view" => handleView()
      case "x" | "exit" => System.exit(0)
      case _ => errorHandler(IllegalArgumentException("Unsupported action."))
    }
  }


  private final def handleNewLoan(): Unit = {
    getLoanDetailsFromUser match {
      case Right(loan) =>
        loanService.add(loan)
        out.println("Loan successfully saved.")
        out.println(loan)
        askUserForAction()
      case Left(ex) =>
        errorHandler(ex)
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
        } yield Loan.fromUserInput(startDate, endDate, amount, currency.getCurrencyCode, baseInterestRate, margin)
      case Left(ex) => Left(ex)
    }
  }

  private final def getStartDate: Either[Throwable, LocalDate] = {
    getUserInput("Please enter the loan start date (format: yyyy-mm-dd): ", str => Try(LocalDate.parse(str)).toEither)
  }

  private final def getEndDate(startDate: LocalDate): Either[Throwable, LocalDate] = {
    val userInput = getUserInput("Please enter the loan end date (format: yyyy-mm-dd): ", str => Try(LocalDate.parse(str)).toEither)
    userInput match {
      case Right(endDate) if endDate.isAfter(startDate) => Right(endDate)
      case _ => Left(new IllegalArgumentException("End date must be after the start date."))
    }
  }

  private final def getAmount: Either[Throwable, BigDecimal] = {
    getUserInput("Loan amount: ", str => Try(BigDecimal(str)).toEither)
  }

  private final def getCurrency: Either[Throwable, Currency] = {
    getUserInput("Currency code: ", str => Try(Currency.getInstance(str.toUpperCase)).toEither)
  }

  private final def getBaseInterestRate: Either[Throwable, BigDecimal] = {
    getUserInput("Base interest rate: ", str => Try(BigDecimal(str)).toEither)
  }

  private final def getMargin: Either[Throwable, BigDecimal] = {
    getUserInput("Margin: ", str => Try(BigDecimal(str)).toEither)
  }

  private def getUserInput[T](prompt: String, validateFn: String => Either[Throwable, T]): Either[Throwable, T] = {
    val inputLoop = LazyList.continually {
      out.println(prompt)
      val input = in.readLine().trim
      validateFn(input)
    }
    inputLoop.dropWhile(_.isLeft).head
  }

  private def handleView(): Unit = {
    loanService.getAllLoans match {
      case Nil =>
        out.println("No previous calculations available.")
        askUserForAction()
      case loans =>
        loans.foreach(loan => out.println(loanService.loanToString(loan._1, loan._2)))
        out.println("Press 'e' to edit a previous loan, or 'b' to go back.")
        LazyList.continually(in.readLine().toLowerCase().trim).foreach {
          case "b" => askUserForAction()
          case "e" => editLoan()
          case _ => errorHandler(IllegalArgumentException("Unsupported action."))
        }
    }
  }

  private def editLoan(): Unit = {
    getUserInput("Enter the loan ID you want to edit: ", str => Try(str.toInt).toEither) match {
      case Right(id) => getLoanDetailsFromUser match {
        case Right(editedLoan: Loan) if loanService.edit(id, editedLoan) =>
          out.println("Loan updated.")
          askUserForAction()
        case Right(_) =>
          errorHandler(new Exception("Failed to update loan."))
        case Left(ex) => errorHandler(ex)
      }
      case _ => errorHandler(IllegalArgumentException("Invalid ID."))
    }
  }

  private def errorHandler(ex: Throwable): Unit = {
    out.println(ex.getMessage)
    askUserForAction()
  }
}

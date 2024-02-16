import models.loan.Loan

import java.time.LocalDate
import java.util.Currency
import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}
import controllers.LoanController


@main
def main(): Unit = {
  val loanController = new LoanController()
  loanController.createLoan()
}


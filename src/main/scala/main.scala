import controllers.LoanController
import models.loan.Loan
import services.DataServiceImpl

import java.io.{BufferedReader, InputStreamReader}
import java.time.LocalDate
import java.util.Currency
import scala.annotation.tailrec
import scala.io.StdIn.readLine

@main
def main(): Unit = {
  val loanService = new DataServiceImpl[Loan]
  val loanController = new LoanController(new BufferedReader(new InputStreamReader(System.in)), System.out, loanService)
  loanController.askUserForAction()
}

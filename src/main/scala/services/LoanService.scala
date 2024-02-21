package services

import models.loan.Loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class LoanService(dataServiceImpl: DataServiceImpl[Loan]) {

  def add(loan: Loan): Unit = {
    dataServiceImpl.add(loan)
  }

  def getAllLoans: List[String] = {
    dataServiceImpl.getAll.map { case (id, loan) =>
      loanToString(id, loan)
    }
  }

  private def loanToString(id: Int, loan: Loan): String = {
    s"""
       |ID: $id
     """.stripMargin
      +
      loan.toString
  }
}

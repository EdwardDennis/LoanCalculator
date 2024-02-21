package services

import models.loan.Loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class LoanService(dataServiceImpl: DataServiceImpl[Loan]) {

  def add(loan: Loan): Unit = {
    dataServiceImpl.add(loan)
  }

  def edit(id: Int, loan: Loan): Boolean = {
    dataServiceImpl.edit(id, loan)
  }

  def getAllLoans: List[(Int, Loan)] = dataServiceImpl.getAll
  
  def loanToString(id: Int, loan: Loan): String = {
    s"""
       |ID: $id
     """.stripMargin
      +
      loan.toString
  }
}

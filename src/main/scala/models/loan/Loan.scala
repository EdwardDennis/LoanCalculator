package models.loan

import java.time.LocalDate

case class Loan(startDate: LocalDate, endDate: LocalDate, amount: BigDecimal, currency: String, baseInterestRate: BigDecimal,
                margin: BigDecimal)

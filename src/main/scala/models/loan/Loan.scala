package models.loan

import java.time.LocalDate

case class Loan(startDate: LocalDate, endDate: LocalDate, amount: BigDecimal, currency: String, baseInterestRate: BigDecimal,
                margin: BigDecimal) {

  override def toString: String = {
    s"Start Date: $startDate, End Date: $endDate, Amount: $amount $currency, Base Interest Rate: $baseInterestRate%, Margin: $margin%"
  }
}

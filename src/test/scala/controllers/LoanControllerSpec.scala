package controllers

import java.io.{BufferedReader, PrintStream}
import org.mockito.Mockito.{reset, times, verify, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.PrivateMethodTester.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.wordspec.AnyWordSpec

import java.time.LocalDate
import java.util.Currency

class LoanControllerSpec extends AnyWordSpec with BeforeAndAfterEach with MockitoSugar {

  private val getStartDate = PrivateMethod[LocalDate](Symbol("getStartDate"))
  private val getEndDate = PrivateMethod[LocalDate](Symbol("getEndDate"))
  private val getAmount = PrivateMethod[BigDecimal](Symbol("getAmount"))
  private val getCurrency = PrivateMethod[Currency](Symbol("getCurrency"))
  private val getBaseInterestRate = PrivateMethod[BigDecimal](Symbol("getBaseInterestRate"))
  private val getMargin = PrivateMethod[BigDecimal](Symbol("getMargin"))

  private val bufferedReader: BufferedReader = mock[BufferedReader]
  private val printStream: PrintStream = mock[PrintStream]
  private val loanController: LoanController = new LoanController(bufferedReader, printStream)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(bufferedReader, printStream)
  }

  "getStartDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn("2022-12-01")

      val startDate = loanController invokePrivate getStartDate()

      assert(startDate.toString == "2022-12-01")
    }
    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")
        .thenReturn("2022-12-01")

      loanController invokePrivate getStartDate()

      verify(printStream).println("Invalid start date format. Please try again.")
    }
  }

  "getEndDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn("2023-12-01")

      val startDate = LocalDate.parse("2022-12-01")
      val endDate = loanController invokePrivate getEndDate(startDate)

      assert(endDate.toString == "2023-12-01")
    }

    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")
        .thenReturn("2023-12-01")

      val startDate = LocalDate.parse("2022-12-01")
      loanController invokePrivate getEndDate(startDate)

      verify(printStream).println("Invalid end date format. Please try again.")
    }

    "not process date earlier than start date" in {
      when(bufferedReader.readLine())
        .thenReturn("2021-12-01")
        .thenReturn("2023-12-01")

      val startDate = LocalDate.parse("2022-12-01")
      loanController invokePrivate getEndDate(startDate)

      verify(printStream).println("End date must be after the start date. Please try again.")
    }
  }

  "getAmount" should {
    "process valid amount input" in {
      when(bufferedReader.readLine()).thenReturn("1000.50")

      val amount = loanController invokePrivate getAmount()

      assert(amount == BigDecimal("1000.50"))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1000.50")

      loanController invokePrivate getAmount()

      verify(printStream).println("Invalid amount. Please try again.")
    }
  }

  "getCurrency" should {
    "process valid currency code input" in {
      when(bufferedReader.readLine()).thenReturn("USD")

      val currency = loanController invokePrivate getCurrency()

      assert(currency.getCurrencyCode == "USD")
    }
    "not process invalid currency code" in {
      when(bufferedReader.readLine())
        .thenReturn("XYZ")
        .thenReturn("USD")

      loanController invokePrivate getCurrency()

      verify(printStream).println("Invalid currency code. Please try again.")
    }
  }

  "getBaseInterestRate" should {
    "process valid base interest rate input" in {
      when(bufferedReader.readLine()).thenReturn("3.5")

      val baseInterestRate = loanController invokePrivate getBaseInterestRate()

      assert(baseInterestRate == BigDecimal("3.5"))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("3.5")

      loanController invokePrivate getBaseInterestRate()

      verify(printStream).println("Invalid base interest amount. Please enter a valid number.")
    }
  }

  "getMargin" should {
    "process valid margin input" in {
      when(bufferedReader.readLine()).thenReturn("1.0")

      val margin = loanController invokePrivate getMargin()

      assert(margin == BigDecimal("1.0"))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1.0")

      loanController invokePrivate getMargin()

      verify(printStream).println("Invalid margin. Please enter a valid number.")
    }
  }
}

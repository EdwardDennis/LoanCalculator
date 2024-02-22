package controllers

import models.loan.Loan
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.PrivateMethodTester.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import services.{DataServiceImpl, LoanService}

import java.io.{BufferedReader, PrintStream}
import java.time.LocalDate
import java.util.Currency

class LoanControllerSpec extends AnyWordSpec with BeforeAndAfterEach with MockitoSugar {

  private val getStartDate = PrivateMethod[Either[Throwable, LocalDate]](Symbol("getStartDate"))
  private val getEndDate = PrivateMethod[Either[Throwable, LocalDate]](Symbol("getEndDate"))
  private val getAmount = PrivateMethod[Either[Throwable, BigDecimal]](Symbol("getAmount"))
  private val getCurrency = PrivateMethod[Either[Throwable, Currency]](Symbol("getCurrency"))
  private val getBaseInterestRate = PrivateMethod[Either[Throwable, BigDecimal]](Symbol("getBaseInterestRate"))
  private val getMargin = PrivateMethod[Either[Throwable, BigDecimal]](Symbol("getMargin"))

  private val bufferedReader: BufferedReader = mock[BufferedReader]
  private val printStream: PrintStream = mock[PrintStream]
  private val loanService = mock[LoanService]
  private val loanController: LoanController = new LoanController(bufferedReader, printStream, loanService)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(bufferedReader, printStream, loanService)
  }

  val startDate: LocalDate = LocalDate.parse("2022-12-01")
  val endDate: LocalDate = LocalDate.parse("2023-12-01")
  
  "getStartDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn(startDate.toString)

      val result = loanController invokePrivate getStartDate()

      assert(result.isRight)
      assert(result.equals(Right(startDate)))

      verify(printStream, times(1)).println("Please enter the loan start date (format: yyyy-mm-dd): ")
      verify(bufferedReader, times(1)).readLine()
    }

    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")
        .thenReturn("2023-12-01")

      val result = loanController invokePrivate getStartDate()

      verify(printStream, times(2)).println("Please enter the loan start date (format: yyyy-mm-dd): ")
      verify(bufferedReader, times(2)).readLine()
    }
  }

  "getEndDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn(endDate.toString)

      val result = loanController invokePrivate getEndDate(startDate)

      assert(result.isRight)
      assert(result.equals(Right(endDate)))

      verify(printStream, times(1)).println("Please enter the loan end date (format: yyyy-mm-dd): ")
      verify(bufferedReader, times(1)).readLine()
    }

    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")
        .thenReturn("2023-12-01")

      val result = loanController invokePrivate getEndDate(startDate)

      verify(printStream, times(2)).println("Please enter the loan end date (format: yyyy-mm-dd): ")
      verify(bufferedReader, times(2)).readLine()
    }

    "not process date earlier than start date" in {
      when(bufferedReader.readLine())
        .thenReturn("2021-12-01")
        .thenReturn("2023-12-01")

      val result = loanController invokePrivate getEndDate(startDate)

      assert(result.isLeft)
      verify(printStream, times(1)).println("Please enter the loan end date (format: yyyy-mm-dd): ")
      verify(bufferedReader, times(1)).readLine()
    }
  }

  "getAmount" should {
    "process valid amount input" in {
      when(bufferedReader.readLine()).thenReturn("1000.50")

      val result = loanController invokePrivate getAmount()

      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("1000.50"))))

      verify(printStream, times(1)).println("Loan amount: ")
      verify(bufferedReader, times(1)).readLine()
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1000.50")

      val result = loanController invokePrivate getAmount()

      verify(printStream, times(2)).println("Loan amount: ")
      verify(bufferedReader, times(2)).readLine()
    }
  }

  "getCurrency" should {
    "process valid currency code input" in {
      when(bufferedReader.readLine()).thenReturn("USD")

      val result = loanController invokePrivate getCurrency()

      assert(result.isRight)
      verify(printStream, times(1)).println("Currency code: ")
      verify(bufferedReader, times(1)).readLine()
    }
    "keep asking until a valid currency code is given" in {
      when(bufferedReader.readLine())
        .thenReturn("XYZ")
        .thenReturn("USD")

      val result = loanController invokePrivate getCurrency()

      verify(printStream, times(2)).println("Currency code: ")
      verify(bufferedReader, times(2)).readLine()
    }
  }

  "getBaseInterestRate" should {
    "process valid base interest rate input" in {
      when(bufferedReader.readLine()).thenReturn("3.5")

      val result = loanController invokePrivate getBaseInterestRate()

      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("3.5"))))

      verify(printStream, times(1)).println("Base interest rate: ")
      verify(bufferedReader, times(1)).readLine()
    }
    "keep asking until a valid decimal is given" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("3.5")

      val result = loanController invokePrivate getBaseInterestRate()

      verify(printStream, times(2)).println("Base interest rate: ")
      verify(bufferedReader, times(2)).readLine()
    }
  }

  "getMargin" should {
    "process valid margin input" in {
      when(bufferedReader.readLine()).thenReturn("1.0")

      val result = loanController invokePrivate getMargin()

      verify(printStream, times(1)).println("Margin: ")
      verify(bufferedReader, times(1)).readLine()
      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("1.0"))))
    }
    "keep asking until a valid decimal is given" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1.0")

      val result = loanController invokePrivate getMargin()

      verify(printStream, times(2)).println("Margin: ")
      verify(bufferedReader, times(2)).readLine()
    }
  }
}

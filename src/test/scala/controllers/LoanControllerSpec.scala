package controllers

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.PrivateMethodTester.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import services.LoanService

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

  "askUserForAction" should {
    "display available commands to the user and handle new loan for valid commands" in {
      val validCommands = List("c", "calc", "calculate")

      for (command <- validCommands) {
        when(printStream.println(any[String])).thenAnswer(_ => ())
        when(bufferedReader.readLine()).thenReturn(command, "2022-12-01", "2023-12-01", "2000", "USD", "3.5", "1.0")

        loanController.askUserForAction()

        verify(printStream).println("Available commands: ")
        verify(printStream).println("- 'c', 'calc', or 'calculate': perform a new loan calculation.")
        verify(bufferedReader, times(7)).readLine()
        reset(printStream, bufferedReader)
      }
    }
  }

  "getStartDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn(startDate.toString)

      val result = loanController invokePrivate getStartDate()

      assert(result.isRight)
      assert(result == Right(LocalDate.of(2022, 12, 1)))
    }
    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")

      val result = loanController invokePrivate getStartDate()
      assert(result.isLeft)
    }
  }

  "getEndDate" should {
    "process valid date input" in {
      when(bufferedReader.readLine()).thenReturn(endDate.toString)

      val result = loanController invokePrivate getEndDate(startDate)

      assert(result.isRight)
      assert(result.equals(Right(endDate)))
    }

    "not process invalid date format" in {
      when(bufferedReader.readLine())
        .thenReturn("notadate")
        .thenReturn("2023-12-01")

      val result = loanController invokePrivate getEndDate(startDate)

      assert(result.isLeft)
    }

    "not process date earlier than start date" in {
      when(bufferedReader.readLine())
        .thenReturn("2021-12-01")
        .thenReturn("2023-12-01")

      val result = loanController invokePrivate getEndDate(startDate)

      assert(result.isLeft)
    }
  }

  "getAmount" should {
    "process valid amount input" in {
      when(bufferedReader.readLine()).thenReturn("1000.50")

      val result = loanController invokePrivate getAmount()

      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("1000.50"))))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1000.50")

      val result = loanController invokePrivate getAmount()

      assert(result.isLeft)
    }
  }

  "getCurrency" should {
    "process valid currency code input" in {
      when(bufferedReader.readLine()).thenReturn("USD")

      val result = loanController invokePrivate getCurrency()

      assert(result.isRight)
      result match {
        case Right(currencyValue) => assert(currencyValue.getCurrencyCode == "USD")
        case _ => fail("Impossible scenario")
      }
    }
    "not process invalid currency code" in {
      when(bufferedReader.readLine())
        .thenReturn("XYZ")
        .thenReturn("USD")

      val result = loanController invokePrivate getCurrency()

      assert(result.isLeft)
    }
  }

  "getBaseInterestRate" should {
    "process valid base interest rate input" in {
      when(bufferedReader.readLine()).thenReturn("3.5")

      val result = loanController invokePrivate getBaseInterestRate()

      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("3.5"))))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("3.5")

      val result = loanController invokePrivate getBaseInterestRate()

      assert(result.isLeft)
    }
  }

  "getMargin" should {
    "process valid margin input" in {
      when(bufferedReader.readLine()).thenReturn("1.0")

      val result = loanController invokePrivate getMargin()

      assert(result.isRight)
      assert(result.equals(Right(BigDecimal("1.0"))))
    }
    "not process invalid input" in {
      when(bufferedReader.readLine())
        .thenReturn("notanumber")
        .thenReturn("1.0")

      val result = loanController invokePrivate getMargin()

      assert(result.isLeft)
    }
  }
}

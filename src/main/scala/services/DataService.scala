package services

import models.loan.Loan

import scala.collection.mutable.ListBuffer

trait DataService[T] {
  def add(t: T): Unit

  def getAll: Seq[T]
}

final class DataServiceImpl[T] extends DataService[T] {
  private val store: ListBuffer[T] = ListBuffer[T]()

  override def add(t: T): Unit = {
    store += t
  }

  override def getAll: List[T] = store.toList
}

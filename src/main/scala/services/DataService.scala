package services

import models.loan.Loan

import scala.collection.mutable.ListBuffer
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.concurrent.TrieMap

trait DataService[T] {
  def add(t: T): Unit

  def getAll: Seq[(Int, T)]
}

final class DataServiceImpl[T] extends DataService[T] {
  private val currentId: AtomicInteger = new AtomicInteger(0)

  private val store: TrieMap[Int, T] = TrieMap.empty[Int, T]

  override def add(t: T): Unit = {
    val nextId = currentId.getAndIncrement()
    store.addOne(nextId -> t)
  }

  def edit(id: Int, t: T): Boolean = {
    store.get(id) match {
      case Some(loan) =>
        store(id) = t
        true
      case None =>
        false
    }
  }

  override def getAll: List[(Int, T)] = store.toList
}

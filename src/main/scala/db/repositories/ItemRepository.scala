package db.repositories

import slick.jdbc.PostgresProfile.api._
import db.models.{Item, Items}
import scala.concurrent.{ExecutionContext, Future}
import db.DatabaseConfig

class ItemRepository(implicit ec: ExecutionContext) {
  private val db = DatabaseConfig.db
  private val items = Items.items

  //  Create a new item
  def create(item: Item): Future[Int] =
    db.run(items returning items.map(_.id) += item)

  //  Get all items
  def getAllItems: Future[Seq[Item]] =
    db.run(items.result)

  //  Get items by user ID
  def getItemsByUser(userId: Int): Future[Seq[Item]] = {
    println(s"🔍 DEBUG: getItemsByUser called with userId = $userId") // Added this

    val query = items.filter(_.ownerId === userId)
    println(s"Executing query for userId: $userId -> SQL: ${query.result.statements.mkString}")

    db.run(query.result).map { result =>
      println(s"✅ Query Result for userId $userId: ${result.mkString(", ")}")
      result
    }
  }


  //  Get an item by ID
  def getById(id: Int): Future[Option[Item]] =
    db.run(items.filter(_.id === id).result.headOption)

  //  Update an item by ID
  def update(id: Int, updatedItem: Item): Future[Option[Item]] = {
    val query = items.filter(_.id === id)
      .map(item => (item.name, item.description))
      .update((updatedItem.name, updatedItem.description))

    db.run(query).flatMap {
      case 1 => getById(id)  // If update successful, return the updated item
      case _ => Future.successful(None)
    }
  }

  //  Delete an item by ID
  def delete(id: Int): Future[Boolean] =
    db.run(items.filter(_.id === id).delete).map(_ > 0)
}

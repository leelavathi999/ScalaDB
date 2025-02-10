package db.repositories

import slick.jdbc.PostgresProfile.api._
import db.models.{Item, Items}
import scala.concurrent.{ExecutionContext, Future}
import db.DatabaseConfig

class ItemRepository(implicit ec: ExecutionContext) {
  private val db = DatabaseConfig.db
  private val items = Items.items

  def create(item: Item): Future[Int] =
    db.run(items returning items.map(_.id) += item)

  def getById(id: Int): Future[Option[Item]] =
    db.run(items.filter(_.id === id).result.headOption)
}

package db.models

import slick.jdbc.PostgresProfile.api._

case class Item(id: Option[Int] = None, name: String, description: String, ownerId: Int)

class Items(tag: Tag) extends Table[Item](tag, "items") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def ownerId = column[Int]("owner_id")

  def owner = foreignKey("owner_fk", ownerId, Users.users)(_.id, onDelete = ForeignKeyAction.Cascade)

  def * = (id.?, name, description, ownerId) <> (Item.tupled, Item.unapply)
}

object Items {
  val items = TableQuery[Items]
}

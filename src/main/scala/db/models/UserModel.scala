package db.models

import slick.jdbc.PostgresProfile.api._

case class User(id: Option[Int] = None, name: String, email: String, hashedPassword: String)

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def email = column[String]("email", O.Unique)
  def hashedPassword = column[String]("hashed_password")

  def * = (id.?, name, email, hashedPassword) <> (User.tupled, User.unapply)
}

object Users {
  val users = TableQuery[Users]
}

package db.repositories

import slick.jdbc.PostgresProfile.api._
import db.models.{User, Users}
import scala.concurrent.{ExecutionContext, Future}
import db.DatabaseConfig
import core.Security

class UserRepository(implicit ec: ExecutionContext) {
  private val db = DatabaseConfig.db
  private val users = Users.users

  //  Create a new user
  def create(user: User): Future[Int] = {
    val hashedPassword = Security.hashPassword(user.hashedPassword) // Make sure password is hashed
    val userWithHashedPassword = user.copy(hashedPassword = hashedPassword)

    db.run(users returning users.map(_.id) += userWithHashedPassword)
  }
  //  Get all users
  def getAllUsers(): Future[Seq[User]] =
    db.run(users.result)

  //  Get a user by ID
  def getById(id: Int): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  //  Update user by ID
  def update(id: Int, updatedUser: User): Future[Option[User]] = {
    val query = users.filter(_.id === id)
      .map(user => (user.name, user.email, user.hashedPassword))
      .update((updatedUser.name, updatedUser.email, updatedUser.hashedPassword))

    db.run(query).flatMap {
      case 1 => getById(id)  // If update successful, return the updated user
      case _ => Future.successful(None)
    }
  }

  //  Delete user by ID
  def delete(id: Int): Future[Boolean] = {
    db.run(users.filter(_.id === id).delete).map(_ > 0)
  }
}

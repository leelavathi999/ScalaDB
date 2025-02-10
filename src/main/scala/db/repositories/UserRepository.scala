package db.repositories

import slick.jdbc.PostgresProfile.api._
import db.models.{User, Users}
import scala.concurrent.{ExecutionContext, Future}
import db.DatabaseConfig

class UserRepository(implicit ec: ExecutionContext) {
  private val db = DatabaseConfig.db
  private val users = Users.users

  def create(user: User): Future[Int] =
    db.run(users returning users.map(_.id) += user)

  def getById(id: Int): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)
}

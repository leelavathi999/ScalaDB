package db

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class Session(implicit ec: ExecutionContext) {
  private val db = DatabaseConfig.db

  def runTransactionally[T](actions: DBIO[T]): Future[T] = db.run(actions.transactionally)
}

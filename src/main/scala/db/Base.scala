package db

import slick.jdbc.PostgresProfile.api._

trait Base {
  val db = DatabaseConfig.db
}

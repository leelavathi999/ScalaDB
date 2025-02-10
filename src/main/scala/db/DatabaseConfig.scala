package db

import slick.jdbc.PostgresProfile.api._
import core.AppConfig
import db.models.{Users,Items}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
object DatabaseConfig {
  lazy val db: Database = Database.forURL(
    url = AppConfig.Database.url,
    user = AppConfig.Database.user,
    password = AppConfig.Database.password,
    driver = AppConfig.Database.driver
  )

  // ✅ Run schema creation when the application starts
  def init(): Unit = {
    val schema = Users.users.schema ++ Items.items.schema
    Await.result(db.run(schema.createIfNotExists), Duration.Inf)
    println("✅ Database schema initialized.")
  }
}

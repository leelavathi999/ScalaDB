package core

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  private val config: Config = ConfigFactory.load()

  object Database {
    val url: String = config.getString("database.url")
    val user: String = config.getString("database.user")
    val password: String = config.getString("database.password")
    val driver: String = config.getString("database.driver")
  }
}

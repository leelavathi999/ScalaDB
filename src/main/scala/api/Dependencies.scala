package api

import db.repositories.{UserRepository, ItemRepository}
import scala.concurrent.ExecutionContext

class Dependencies(implicit ec: ExecutionContext) {
  val userRepository = new UserRepository()
  val itemRepository = new ItemRepository()
}

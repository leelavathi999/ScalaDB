package api.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import db.repositories.ItemRepository
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import db.models.Item
import spray.json._
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.annotation.nowarn
class ItemRoutes(itemRepo: ItemRepository)(implicit ec: scala.concurrent.ExecutionContext) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  @nowarn("msg=Implicit definition should have explicit type")
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat4(Item)


  val route: Route =
    pathPrefix("items") {
      concat(
        get {
          path(IntNumber) { id =>
            onComplete(itemRepo.getById(id)) {
              case Success(Some(item)) => complete(item)
              case Success(None)       => complete(StatusCodes.NotFound, s"Item with ID $id not found")
              case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      )
    }
}

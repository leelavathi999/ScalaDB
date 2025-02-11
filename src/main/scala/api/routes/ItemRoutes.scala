package api.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import db.repositories.ItemRepository
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import db.models.Item
import spray.json._
import scala.util.{Success, Failure}
import scala.annotation.nowarn

class ItemRoutes(itemRepo: ItemRepository)(implicit ec: scala.concurrent.ExecutionContext) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  @nowarn("msg=Implicit definition should have explicit type")
  implicit val itemFormat: RootJsonFormat[Item] = jsonFormat4(Item)

  val route: Route =
    pathPrefix("items") {
      concat(
        //  Create a new item (POST /items)
        post {
          entity(as[Item]) { item =>
            onComplete(itemRepo.create(item)) {
              case Success(itemId) => complete(StatusCodes.Created, s"Item created with ID: $itemId")
              case Failure(ex)     => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },

        //  Get all items (GET /items)
        get {
          onComplete(itemRepo.getAllItems()) {
            case Success(items) => complete(items)
            case Failure(ex)    => complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        },

        //  Get items by user ID (GET /items/user/{user_id})
        get {
          path("user" / IntNumber) { userId =>
            onComplete(itemRepo.getItemsByUser(userId)) {
              case Success(items) => complete(items)
              case Failure(ex)    => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },

        //  Get item by ID (GET /items/{id})
        get {
          path(IntNumber) { id =>
            onComplete(itemRepo.getById(id)) {
              case Success(Some(item)) => complete(item)
              case Success(None)       => complete(StatusCodes.NotFound, s"Item with ID $id not found")
              case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },

        // ✅ Update item by ID (PUT /items/{id})
        put {
          path(IntNumber) { id =>
            entity(as[Item]) { updatedItem =>
              onComplete(itemRepo.update(id, updatedItem)) {
                case Success(Some(item)) => complete(item)
                case Success(None)       => complete(StatusCodes.NotFound, s"Item with ID $id not found")
                case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
              }
            }
          }
        },

        // ✅ Delete item by ID (DELETE /items/{id})
        delete {
          path(IntNumber) { id =>
            onComplete(itemRepo.delete(id)) {
              case Success(true)  => complete(StatusCodes.OK, "Item deleted successfully")
              case Success(false) => complete(StatusCodes.NotFound, s"Item with ID $id not found")
              case Failure(ex)    => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      )
    }
}

package api.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import db.repositories.UserRepository
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import db.models.User
import spray.json._
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.annotation.nowarn
class UserRoutes(userRepo: UserRepository)(implicit ec: scala.concurrent.ExecutionContext) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  @nowarn("msg=Implicit definition should have explicit type")
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User)

  val route: Route =
    pathPrefix("users") {
      concat(
        get {
          path(IntNumber) { id =>
            onComplete(userRepo.getById(id)) {
              case Success(Some(user)) => complete(user)
              case Success(None)       => complete(StatusCodes.NotFound, s"User with ID $id not found")
              case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      )
    }
}

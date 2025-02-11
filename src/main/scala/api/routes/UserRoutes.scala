package api.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import db.repositories.UserRepository
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import db.models.User
import spray.json._
import scala.util.{Success, Failure}
import scala.annotation.nowarn

class UserRoutes(userRepo: UserRepository)(implicit ec: scala.concurrent.ExecutionContext) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  @nowarn("msg=Implicit definition should have explicit type")
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User)

  val route: Route =
    pathPrefix("users") {
      concat(
        // Create a new user (POST /users)
        post {
          entity(as[User]) { user =>
            onComplete(userRepo.create(user)) {
              case Success(userId) => complete(StatusCodes.Created, s"User created with ID: $userId")
              case Failure(ex)     => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },

        //  Get all users (GET /users)
        get {
          onComplete(userRepo.getAllUsers()) {
            case Success(users) => complete(users)
            case Failure(ex)    => complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        },

        // Get a user by ID (GET /users/{id})
        get {
          path(IntNumber) { id =>
            onComplete(userRepo.getById(id)) {
              case Success(Some(user)) => complete(user)
              case Success(None)       => complete(StatusCodes.NotFound, s"User with ID $id not found")
              case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        },

        //  Update user by ID (PUT /users/{id})
        put {
          path(IntNumber) { id =>
            entity(as[User]) { updatedUser =>
              onComplete(userRepo.update(id, updatedUser)) {
                case Success(Some(user)) => complete(user)
                case Success(None)       => complete(StatusCodes.NotFound, s"User with ID $id not found")
                case Failure(ex)         => complete(StatusCodes.InternalServerError, ex.getMessage)
              }
            }
          }
        },

        //  Delete user by ID (DELETE /users/{id})
        delete {
          path(IntNumber) { id =>
            onComplete(userRepo.delete(id)) {
              case Success(true)  => complete(StatusCodes.OK, "User deleted successfully")
              case Success(false) => complete(StatusCodes.NotFound, s"User with ID $id not found")
              case Failure(ex)    => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      )
    }
}

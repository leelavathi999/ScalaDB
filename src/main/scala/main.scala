package main

import akka.http.scaladsl.Http
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.routes.{UserRoutes, ItemRoutes}
import db.repositories.{UserRepository, ItemRepository}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import db.DatabaseConfig // ✅ Import DatabaseConfig

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "ScalaSlickApp")
    implicit val ec: ExecutionContextExecutor = system.executionContext
    // ✅ Initialize database schema
    DatabaseConfig.init()

    val userRepo = new UserRepository()
    val itemRepo = new ItemRepository()

    // ✅ Add a default route for "/"
    val defaultRoute: Route =
      pathSingleSlash {
        get {
          complete("Welcome to Scala Slick API!")
        }
      }

    // ✅ Include the default route in concat
    val routes = concat(
      defaultRoute,
      new UserRoutes(userRepo).route,
      new ItemRoutes(itemRepo).route
    )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)

    println("Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

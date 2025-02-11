name := "ScalaDB"
version := "0.1"
scalaVersion := "2.13.12"  // Downgraded to Scala 2.13 for better Akka & Slick support

//  Enable Coursier for dependency resolution
ThisBuild / useCoursier := true

// Library dependencies
libraryDependencies ++= Seq(
  //  Slick for database access
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.4", // PostgreSQL driver

  //  Akka HTTP Core (Compatible Version)
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
  "com.typesafe.akka" %% "akka-stream" % "2.8.5",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",  //  JSON support for Akka HTTP

  //  Circe for JSON parsing
  "io.circe" %% "circe-core" % "0.14.5",
  "io.circe" %% "circe-generic" % "0.14.5",
  "io.circe" %% "circe-parser" % "0.14.5",

  //  BCrypt for password hashing
  "org.mindrot" % "jbcrypt" % "0.4",

  //  Logging
  "ch.qos.logback" % "logback-classic" % "1.2.11",

//  tapir ui
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % "1.9.6",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.9.6"
)

//  Resolvers for additional dependencies
resolvers ++= Seq(
  "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

package com.example

import java.util.concurrent.CountDownLatch

import play.api._
import play.api.mvc._
import play.core.DefaultWebCommands
import play.core.server.{Server, ServerConfig}

object Main extends App {

  val context = ApplicationLoader.Context(Environment.simple(), None, new DefaultWebCommands(), Configuration.from(Map(
    "play.crypto.secret" -> "changeme",
    "play.crypto.provider" -> "",
    "play.crypto.aes.transformation" -> "",
    "play.http.context" -> "/",
    "play.http.parser.maxMemoryBuffer" -> "2MB",
    "play.http.parser.maxDiskBuffer" -> "10MB",
    "play.http.actionComposition.controllerAnnotationsFirst" -> false,
    "play.http.actionComposition.executeActionCreatorActionFirst" -> false,
    "play.http.cookies.strict" -> true,
    "play.http.session.cookieName" -> "",
    "play.http.session.secure" -> true,
    "play.http.session.maxAge" -> null,
    "play.http.session.httpOnly" -> true,
    "play.http.session.domain" -> "",
    "play.http.flash.cookieName" -> "",
    "play.http.flash.secure" -> true,
    "play.http.flash.httpOnly" -> true,
    "play.http.forwarded" -> Map("version" -> "x-forwarded", "trustedProxies" -> List()),
    "play.akka.config" -> "akka",
    "play.akka.actor-system" -> "hello",
    "akka" -> Map()
  )))

  val app = new BuiltInComponentsFromContext(context) {
    import play.api.routing.Router
    import play.api.routing.sird._

    override def router = Router.from {
      case GET(p"/hello/$to") => Action {
        Results.Ok(s"Hello $to")
      }
    }
  }.application

  Server.withApplication(app, ServerConfig(port = Some(8000))) { _ =>
    new CountDownLatch(1).await()
  }
}

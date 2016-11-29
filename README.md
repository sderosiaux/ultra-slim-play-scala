# Ultra-slim-play-app

The idea is to start an Play! application as any classic application, meaning, without `app/` or `application.conf` magic.
It's inspired from https://github.com/lloydmeta/slim-play, but it's going further by removing more Play! dependencies.

Basically, everything is in one file, a `Main`, without external references to any configuration.
It doesn't even run in `app/` anymore, but it's just a classic Scala `Main extends App` to start.

Play! version: 2.5.10

# Code

Here is the whole code in `src`:
 
```scala
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
```

All those properties are the strict minimum for Play! to start. (it's mostly `HttpConfiguration()`)
I'm sure we can bypass that and provide the configuration using the default `case class`es, but I didn't look anymore. You can PR if you find out.

We start the application ourselves and wait indefinitely at the end.

# How to

Start the `Main` yourself in Intellij or however you want. You'll see some debug logs (mostly netty) and the HTTP server starting:
```
02:29:55.975 [main] INFO play.core.server.NettyServer - Listening for HTTP on /0:0:0:0:0:0:0:0:8000
```

It works!
```
$ curl http://localhost:8000/hello/you
Hello you
```

# Note

There is still an empty `application.conf` in `conf/` because Play! checks for its existence. :-( 

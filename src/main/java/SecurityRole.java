import io.javalin.security.RouteRole;

public enum SecurityRole implements RouteRole {
  ANYONE,
  USER,
  ADMIN
}

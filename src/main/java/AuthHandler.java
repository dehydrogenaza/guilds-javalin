import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;

public class AuthHandler {
  private static final Logger LOG = LoggerFactory.getLogger(AuthHandler.class);

  private static final Map<Credentials, SecurityRole> USERS = Map.of(
      new Credentials("user", "user"), SecurityRole.USER,
      new Credentials("admin", "admin"), SecurityRole.ADMIN);

  public static void checkAccess(Context ctx) {
    Set<RouteRole> requiredRoles = ctx.routeRoles();
    if (requiredRoles.contains(SecurityRole.ANYONE)) {
      LOG.info("Access granted to anyone");
      return;
    }

    var credentials = ctx.basicAuthCredentials();
    if (credentials == null) {
      LOG.info("Missing credentials");
      ctx.status(401);
      ctx.header(Header.WWW_AUTHENTICATE, "Basic");
      throw new UnauthorizedResponse("Credentials are required");
    }

    SecurityRole userRole = USERS.get(new Credentials(credentials.getUsername(), credentials.getPassword()));
    if (userRole == null || !requiredRoles.contains(userRole)) {
      LOG.info("No required role {} for user: {}", requiredRoles, credentials.getUsername());
      ctx.status(403);
      throw new ForbiddenResponse("Insufficient permissions");
    }
  }

  record Credentials(
      String username,
      String password
  ) {}
}

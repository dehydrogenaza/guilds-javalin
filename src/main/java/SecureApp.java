import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.Context;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SecureApp {
  private static final Logger LOG = LoggerFactory.getLogger(SecureApp.class);
  private static final EmployeeDao EMPLOYEE_DAO = new EmployeeDao();

  public static void main(String[] args) {
    var app = Javalin.create(config -> {
//      config.registerPlugin(configureSsl());

      config.router.mount(router -> router.beforeMatched(AuthHandler::checkAccess));

      config.router.apiBuilder(() -> {
        get("/", ctx -> ctx.redirect("/hello"), SecurityRole.ANYONE);
        get("/hello", ctx -> ctx.result("Hello, Javalin!"), SecurityRole.ANYONE);
        path("employees", () -> {
          get(SecureApp::getEmployees, SecurityRole.USER, SecurityRole.ADMIN);
          delete("{id}", SecureApp::deleteEmployee, SecurityRole.ADMIN);
        });
      });
    });

    app.start(8080);

    LOG.info("Secure server started!");
  }

  private static void getEmployees(Context ctx) {
    ctx.json(EMPLOYEE_DAO.findAll());
  }

  private static void deleteEmployee(Context ctx) {
    var id = ctx.pathParamAsClass("id", Integer.class)
        .get();
    EMPLOYEE_DAO.delete(id);
    ctx.status(204);
  }

  private static SslPlugin configureSsl() {
    return new SslPlugin(config -> {
      config.pemFromClasspath("ssl/cert.pem", "ssl/key.pem");
      config.sniHostCheck = false;
    });
  }
}

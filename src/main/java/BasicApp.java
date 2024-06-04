import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class BasicApp {
  private static final Logger LOG = LoggerFactory.getLogger(BasicApp.class);
  private static final EmployeeDao EMPLOYEE_DAO = new EmployeeDao();

  public static void main(String[] args) {
    var app = Javalin.create(config -> {
      config.useVirtualThreads = true;

      config.requestLogger.http((ctx, ms) -> LOG.info("{} {} took {} ms", ctx.method(), ctx.path(), ms));
    });

    app.get("/", ctx -> ctx.redirect("/hello"));
    app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
    app.get("/employees", ctx -> ctx.json(EMPLOYEE_DAO.findAll()));
    app.get("/employees/{id}", BasicApp::getEmployee);
    app.post("/employees", BasicApp::addEmployee);

    app.start(7070);

    LOG.info("Server started!");
  }

  private static void getEmployee(Context ctx) {
    var id = ctx.pathParamAsClass("id", Integer.class)
        .get();
    var employee = EMPLOYEE_DAO.findById(id)
        .orElseThrow(() -> new NotFoundResponse("Employee not found"));
    ctx.json(employee);
  }

  private static void addEmployee(Context ctx) {
    var employee = ctx.bodyAsClass(Employee.class);
    EMPLOYEE_DAO.save(employee);
    ctx.status(201);
  }
}

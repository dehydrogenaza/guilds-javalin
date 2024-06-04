import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class WebSocketApp {
  private static final Logger LOG = LoggerFactory.getLogger(BasicApp.class);

  public static void main(String[] args) {
    var app = Javalin.create(config -> {
      config.staticFiles.add("/public", Location.CLASSPATH);
      config.staticFiles.enableWebjars();
    });

    app.ws("/chat", ws -> ws.onMessage(ctx -> {
      LOG.info("Received message: {}", ctx.message());
      ctx.send(ctx.message().toUpperCase());
    }));

    app.start(9090);
  }
}

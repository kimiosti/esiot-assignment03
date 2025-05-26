package unibo.esiot2024.http;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import unibo.esiot2024.central.api.CentralController;

/**
 * HTTP server for the control unit.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            This class must carry a reference to a central controller instance to access the database and make
            HTTP requests effective.
            """
)
 public final class HTTPServer extends AbstractVerticle {

    private static final String HOME = "/index.html";
    private static final String HOME_STYLE = "/style/style.css";
    private static final String HOME_SCRIPT = "/scripts/index.js";
    private static final String CONTENT_TYPE_LABEL = "content-type";
    private static final String HTML_CONTENT = "text/html";
    private static final String CONTENT_FOLDER = "webapp";
    private static final String OPENING_INPUT = "/api/action/setOpening";
    private static final String TOGGLE_MODE = "/api/action/toggleMode";
    private static final String RESTORE = "/api/action/restoreState";
    private static final String GET_DATA = "/api/data";
    private static final float DEFAULT_TEMP = 0f;
    private static final int DEFAULT_OPENING = 0;
    private static final String DEFAULT_STATE = "unknown";
    private static final int SERVER_PORT = 8080;

    private final CentralController controller;

    /**
     * Intantiates an HTTP server.
     * @param controller the {@link CentralController} instance managing system data.
     */
    public HTTPServer(final CentralController controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        final var router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        this.setGetRoute(router, HOME, true);
        this.setGetRoute(router, HOME_STYLE, false);
        this.setGetRoute(router, HOME_SCRIPT, false);

        router.route(HttpMethod.POST, OPENING_INPUT).handler(routingContext -> {
            final var content = routingContext.getBodyAsString().split("=");

            if (content.length == 2) {
                this.controller.setOpeningLevel(Integer.parseInt(content[1]));
            }

            routingContext.redirect(HOME);
        });

        router.route(HttpMethod.POST, TOGGLE_MODE).handler(routingContext -> {
            this.controller.switchOperativeMode();
            routingContext.redirect(HOME);
        });

        router.route(HttpMethod.POST, RESTORE).handler(routingContext -> {
            this.controller.restoreIssue();
            routingContext.redirect(HOME);
        });

        router.route(HttpMethod.POST, GET_DATA).handler(routingContext -> {
            final var avg = this.controller.getAverageTemperature();
            final var max = this.controller.getMaxTemperature();
            final var min = this.controller.getMinTemperature();
            final var info = this.controller.getCurrentValues();
            final var measures = this.controller.getLastMeasures();

            final var temps = new ArrayList<Float>();
            final var times = new ArrayList<String>();
            for (final var measure : measures) {
                temps.add(measure.temperature());
                times.add(measure.time().toString());
            }

            routingContext.json(
                new JsonObject()
                    .put("average", avg.isPresent() ? avg.get() : DEFAULT_TEMP)
                    .put("max", max.isPresent() ? max.get() : DEFAULT_TEMP)
                    .put("min", min.isPresent() ? min.get() : DEFAULT_TEMP)
                    .put("state", info.isPresent() ? info.get().state().getState() : DEFAULT_STATE)
                    .put("opening", info.isPresent() ? info.get().openingPercentage() : DEFAULT_OPENING)
                    .put("temps", temps)
                    .put("times", times)
            );
        });

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(SERVER_PORT)
            .onSuccess(server -> {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
                    Level.INFO,
                    "Server open on port " + server.actualPort()
                );
            });
    }

    private void setGetRoute(final Router router, final String route, final boolean isHTML) {
        router.route(HttpMethod.GET, route).handler(routingContext -> {
            var response = routingContext.response();

            if (isHTML) {
                response = response.putHeader(CONTENT_TYPE_LABEL, HTML_CONTENT);
            }

            response.sendFile(CONTENT_FOLDER + route);
        });
    }
}

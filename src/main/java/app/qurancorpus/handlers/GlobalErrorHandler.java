package app.qurancorpus.handlers; // Replace with your package name

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GlobalErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @Error(global = true)
    public HttpResponse<String> handleErrors(HttpRequest<?> request, Throwable ex) {
        LOG.error("Error occurred: ", ex);
        // Customize the response based on the exception
        return HttpResponse.<String>serverError()
                .body("Unexpected error: " + ex.getMessage());
    }
}

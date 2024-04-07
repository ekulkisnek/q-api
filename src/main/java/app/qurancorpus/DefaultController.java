package app.qurancorpus; // Replace with your actual package name

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class DefaultController {

    @Get("/")
    public String index() {
        return "Hello, World!";
    }
}

package app.web;

import app.web.dto.GetRouteDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
public interface ICountriesController {

    @GetMapping("/routing/{origin}/{destination}")
    GetRouteDto getRoute(@PathVariable String destination,
                                  @PathVariable String origin,
                                  HttpServletResponse response);
}

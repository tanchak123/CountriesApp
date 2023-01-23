package app.web;

import app.data.exceptions.UnreachableRouteException;
import app.service.CountryService;
import app.web.dto.GetRouteDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountriesController implements ICountriesController {

    @Autowired
    CountryService countryService;


    @Override
    public GetRouteDto getRoute(String destination, String origin, HttpServletResponse response) {
        List<String> routesFromOriginToDestination;
        try {
            routesFromOriginToDestination = countryService.getRoutesFromOriginToDestination(destination, origin);
        } catch (UnreachableRouteException e) {
            return new GetRouteDto(e.getMessage());
        }

        return new GetRouteDto(routesFromOriginToDestination);
    }
}

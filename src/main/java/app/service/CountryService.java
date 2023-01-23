package app.service;

import app.model.Country;
import app.service.helper.CountryServiceHelper;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.data.constants.URLS.COUNTRIES;

@Service
@Slf4j
public class CountryService {
    private final Map<String, Country> ccaThreeAndCountryMap;

    private final HttpClientService httpClientService;

    @Autowired
    public CountryService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
        this.ccaThreeAndCountryMap = initializeCountries();
        log.info("CountriesService was initialized : {}", ccaThreeAndCountryMap);
    }

    private Map<String, Country> initializeCountries() {
        log.trace("Trying to initialize countries");
        String countries = httpClientService.doGet(COUNTRIES);

        return JSON.parseArray(countries, Country.class).stream().collect(Collectors.toMap(
                Country::getCca3, it -> it)
        );
    }

    public List<String> getRoutesFromOriginToDestination(String destination, String origin) {


        return CountryServiceHelper.getRoutesFromOriginToDestination(destination, origin, ccaThreeAndCountryMap);
    }
}

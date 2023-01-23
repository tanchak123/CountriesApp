package app.service;

import app.data.exceptions.HttpException;
import app.model.Country;
import app.service.helper.CountryServiceHelper;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static app.data.constants.URLS.COUNTRIES;

@Service
@Slf4j
public class CountryService {
    private final static int FAIL_RETRY_S = 10;
    private final Map<String, Country> ccaThreeAndCountryMap = new HashMap<>();
    private final HttpClientService httpClientService;
    private final Scheduler scheduler = Schedulers.boundedElastic();

    @Autowired
    public CountryService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
        initializeCountries();
    }

    private void initializeCountries() {
        log.info("Trying to initialize countries");
        String countries;
        try {
            countries = httpClientService.doGet(COUNTRIES);
        } catch (Exception exception) {
            log.error("Can't get initialize countries because {}. Retry in {} seconds",
                    exception.getMessage(), FAIL_RETRY_S);
            scheduler.schedule(this::initializeCountries, FAIL_RETRY_S, TimeUnit.SECONDS);
            return;
        }

        ccaThreeAndCountryMap.putAll(JSON.parseArray(countries, Country.class).stream().collect(Collectors.toMap(
                Country::getCca3, it -> it)
        ));
    }

    public List<String> getRoutesFromOriginToDestination(String destination, String origin) {

        return CountryServiceHelper.getRoutesFromOriginToDestination(destination, origin, ccaThreeAndCountryMap);
    }
}

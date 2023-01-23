package app.service.helper;

import app.data.exceptions.UnreachableRouteException;
import app.data.objects.CountryNode;
import app.data.objects.Pair;
import app.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static app.data.constants.URLS.COUNTRIES;

@Slf4j
public class CountryServiceHelper {

    private static final Map<Pair<String, String>, CountryNode> originDestinationCountryNodeMap = new HashMap<>();

    public static List<String> getRoutesFromOriginToDestination(String destination,
                                                                String origin,
                                                                Map<String, Country> ccaThreeAndCountryMap) {
        List<String> routeResult = new ArrayList<>();

        Country start = ccaThreeAndCountryMap.get(origin);
        if (start == null || ccaThreeAndCountryMap.get(destination) == null) {
            return routeResult;
        }

        if (destination.equals(origin)) {
            log.warn("destination: {} and origin: {} are the same", destination, origin);
            return routeResult;
        }

        Pair<String, String> originDestination = new Pair<>(origin, destination);

        CountryNode countryNode = originDestinationCountryNodeMap.get(originDestination);

        if (countryNode == null) {
            getRoute(ccaThreeAndCountryMap, originDestination);
        }

        countryNode = originDestinationCountryNodeMap.get(originDestination);
        while (countryNode != null) {
            routeResult.add(countryNode.getCca3());
            countryNode = countryNode.getNext();
        }

        return routeResult;
    }

    private static void getRoute(Map<String, Country> ccaThreeAndCountryMap,
                                 Pair<String, String> originDestination) {
        Map<String, CountryNode> countryNameNodeMap = new HashMap<>();
        Country currentCountry = ccaThreeAndCountryMap.get(originDestination.getFirst());

        Map<CountryNode, List<Country>> currentCountryNodeMap = new HashMap<>();
        currentCountryNodeMap.put(new CountryNode(null, null, originDestination.getFirst()),
                currentCountry.getBorders().stream().map(ccaThreeAndCountryMap::get).collect(Collectors.toList()));

        getRouteAlgo(ccaThreeAndCountryMap,countryNameNodeMap,  currentCountryNodeMap, originDestination);

    }

    private static void getRouteAlgo(Map<String, Country> ccaThreeAndCountryMap,
                                     Map<String, CountryNode> globalCountryNameNodeMap,
                                     Map<CountryNode, List<Country>> currentCountryNodeMap,
                                     Pair<String, String> originDestination) {
        log.info("current root countries : {}", currentCountryNodeMap
                .keySet()
                .stream()
                .map(CountryNode::getCca3)
                .collect(Collectors.toList()));

        Map<String, CountryNode> localRootsNameNodeMap = new HashMap<>();
        currentCountryNodeMap.keySet()
                .forEach(key -> {
                    List<Country> countries = currentCountryNodeMap.get(key);
                    countries.forEach(country -> localRootsNameNodeMap.put(country.getCca3(), key));
                });

        Set<String> availableBorders = currentCountryNodeMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .map(Country::getCca3).collect(Collectors.toSet())
                .stream()
                .filter(borderName -> !globalCountryNameNodeMap.containsKey(borderName))
                .collect(Collectors.toSet());

        if (availableBorders.size() == 0) {
            throw new UnreachableRouteException(String.format("Unreachable Route from %s to %s",
                    originDestination.getFirst(), originDestination.getSecond()));
        }

        if (availableBorders.contains(originDestination.getSecond())) {
            CountryNode successNode = new CountryNode(null,
                    localRootsNameNodeMap.get(originDestination.getSecond()),
                    originDestination.getSecond());
            routeWasFound(originDestination, successNode);
        }

        if (originDestinationCountryNodeMap.get(originDestination) == null) {
            Map<CountryNode, List<Country>> newCurrentNodeCountriesMap = extendRootLevel(ccaThreeAndCountryMap,
                    globalCountryNameNodeMap,
                    localRootsNameNodeMap,
                    availableBorders);

            getRouteAlgo(
                    ccaThreeAndCountryMap,
                    globalCountryNameNodeMap,
                    newCurrentNodeCountriesMap,
                    originDestination
            );
        }
    }


    private static Map<CountryNode, List<Country>> extendRootLevel(Map<String, Country> ccaThreeAndCountryMap,
                                                                   Map<String, CountryNode> globalCountryNameNodeMap,
                                                                   Map<String, CountryNode> localNameNodeMap,
                                                                   Set<String> availableBorders) {
        availableBorders.forEach(border -> globalCountryNameNodeMap.put(border,
                        new CountryNode(null, localNameNodeMap.get(border), border)));
        Map<CountryNode, List<Country>> newCurrentNodeCountriesMap = new HashMap<>();

        Set<String> checkedInnerBorders = new HashSet<>();
        availableBorders.forEach(it -> {
                Country country = ccaThreeAndCountryMap.get(it);
                CountryNode countryNode = globalCountryNameNodeMap.get(it);
                country.getBorders().forEach(innerBorder -> {
                    if (!globalCountryNameNodeMap.containsKey(innerBorder) && !checkedInnerBorders.contains(innerBorder)) {

                        checkedInnerBorders.add(innerBorder);
                        newCurrentNodeCountriesMap.putIfAbsent(countryNode, new ArrayList<>());
                        newCurrentNodeCountriesMap.get(countryNode).add(ccaThreeAndCountryMap.get(innerBorder));
                    }
                });
        });

        return newCurrentNodeCountriesMap;
    }

    private static void routeWasFound(Pair<String, String> originDestination, CountryNode successNode) {
        while (true) {
            if (successNode.getPrevious() == null) {
                originDestinationCountryNodeMap.put(originDestination, successNode);
                break;
            }
            successNode.getPrevious().setNext(successNode);
            successNode = successNode.getPrevious();
        }

        originDestinationCountryNodeMap.put(originDestination, successNode);
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(COUNTRIES, String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }
}

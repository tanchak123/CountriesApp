package app.model;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class Country {
    String cca3;
    String name;
    List<String> borders;

}

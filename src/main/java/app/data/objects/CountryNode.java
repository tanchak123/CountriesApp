package app.data.objects;

import app.model.Country;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public class CountryNode {
    @Setter
    @NonFinal
    CountryNode next;
    CountryNode previous;
    String cca3;


    public CountryNode(CountryNode next, CountryNode previous, String cca3) {
        this.next = next;
        this.previous = previous;
        this.cca3 = cca3;
    }



    @Override
    public String toString() {
        return "CountryNode{" +
                ", cca3='" + cca3 + '\'' +
                '}';
    }
}

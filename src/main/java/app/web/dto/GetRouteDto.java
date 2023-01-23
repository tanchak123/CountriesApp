package app.web.dto;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

import static app.data.constants.HttpCodes.FAIL;
import static app.data.constants.HttpCodes.SUCCESS;

@Value
public class GetRouteDto {
    int status;
    String message;
    @NonFinal
    List<String> route = null;

    public GetRouteDto(List<String> route) {
        if (route.size() == 0) {
            this.status = FAIL;
            this.message = "Client problem";
        } else {
            this.status = SUCCESS;
            this.message = "Success";
        }

        this.route = route;
    }

    public GetRouteDto(String message) {
        this.message = message;
        this.status = 200;
    }
}

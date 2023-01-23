package app.data.exceptions;

public class UnreachableRouteException extends HttpException {
    public UnreachableRouteException(String message) {
        super(message);
    }
}

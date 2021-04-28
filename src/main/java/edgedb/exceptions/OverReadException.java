package edgedb.exceptions;

public class OverReadException extends RuntimeException {
    String message = "Over reading DataInputStream";
}

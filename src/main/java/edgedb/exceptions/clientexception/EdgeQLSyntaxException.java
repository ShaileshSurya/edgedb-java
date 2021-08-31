package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class EdgeQLSyntaxException extends BaseException {
    public EdgeQLSyntaxException(){
        super("EdgeQLSyntaxError");
    }
}

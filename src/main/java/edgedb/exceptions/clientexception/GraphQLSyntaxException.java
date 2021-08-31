package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class GraphQLSyntaxException extends BaseException {
    public GraphQLSyntaxException(){
        super("GraphQLSyntaxError");
    }
}

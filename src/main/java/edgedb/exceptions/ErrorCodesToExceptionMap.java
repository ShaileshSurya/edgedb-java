package edgedb.exceptions;

import edgedb.exceptions.clientexception.*;

import java.util.*;

public interface ErrorCodesToExceptionMap {

    public static final Map<Integer, BaseException> errorCodesMap = initMap();

    public static Map<Integer, BaseException> initMap() {
        Map<Integer, BaseException> map = new HashMap<>();
        map.put(0x01000000, new InternalServerException());

        map.put(0x02000000, new UnsupportedFeatureException());

        map.put(0x03000000, new ProtocolException());
        map.put(0x03010000, new BinaryProtocolException());
        map.put(0x03010001, new UnsupportedProtocolVersionException());
        map.put(0x03010002, new TypeSpecNotFoundException());
        map.put(0x03010003, new UnexpectedMessageException());
        map.put(0x03020000, new InputDataException());
        map.put(0x03030000, new ResultCardinalityMismatchException());
        map.put(0x03040000, new CapabilityException());
        map.put(0x03040100, new UnsupportedCapabilityException());
        map.put(0x03040200, new DisabledCapabilityException());


        map.put(0x04000000, new QueryException());

        map.put(0x04010000, new InvalidSyntaxException());
        map.put(0x04010100, new EdgeQLSyntaxException());
        map.put(0x04010200, new SchemaSyntaxException());
        map.put(0x04010300, new GraphQLSyntaxException());


        map.put(0x04030000, new InvalidReferenceException());
        map.put(0x04030001, new UnknownModuleException());
        map.put(0x04030002, new UnknownLinkException());
        map.put(0x04030003, new UnknownPropertyException());
        map.put(0x04030004, new UnknownUserException());
        map.put(0x04030005, new UnknownDatabaseException());
        map.put(0x04030006, new UnknownLinkException());


        map.put(0x04040000, new SchemaException());

        map.put(0x04050000, new SchemaDefinitionException());

        map.put(0x04050100, new InvalidDefinitionException());
        map.put(0x04050101, new InvalidModuleDefinitionException());
        map.put(0x04050102, new InvalidLinkDefinitionException());
        map.put(0x04050103, new InvalidPropertyDefinitionException());
        map.put(0x04050104, new InvalidUserDefinitionException());
        map.put(0x04050105, new  InvalidDatabaseDefinitionException());
        map.put(0x04050106, new InvalidOperatorDefinitionException());
        map.put(0x04050107, new InvalidAliasDefinitionException());
        map.put(0x04050108, new InvalidFunctionDefinitionException());
        map.put(0x04050109, new InvalidConstraintDefinitionException());
        map.put(0x0405010A, new InvalidCastDefinitionException());


        map.put(0x04050200, new DuplicateDefinitionException());
        map.put(0x04050201, new DuplicateModuleDefinitionException());
        map.put(0x04050202, new DuplicateLinkDefinitionException());
        map.put(0x04050203, new DuplicatePropertyDefinitionException());
        map.put(0x04050204, new DuplicateUserDefinitionException());
        map.put(0x04050205, new DuplicateDatabaseDefinitionException());
        map.put(0x04050206, new DuplicateOperatorDefinitionException());
        map.put(0x04050207, new DuplicateViewDefinitionException());
        map.put(0x04050208, new DuplicateFunctionDefinitionException());
        map.put(0x004050209, new DuplicateConstraintDefinitionException());
        map.put(0x0405020A, new DuplicateCastDefinitionException());


        map.put(0x04060000, new QueryTimeoutException());


        map.put(0x05000000, new ExecutionException());
        map.put(0x05010000, new InvalidValueException());
        map.put(0x05010001, new DivisionByZeroException());
        map.put(0x05010002, new NumericOutOfRangeException());

        map.put(0x05020000, new IntegrityException());
        map.put(0x05020001, new ConstraintViolationException());
        map.put(0x05020002, new CardinalityViolationException());
        map.put(0x05020003, new MissingRequiredException());


        map.put(0x06000000, new ConfigurationException());

        map.put(0x07000000, new AccessException());
        map.put(0x07010000, new AuthenticationException());

        //TODO add more exceptions

        return Collections.unmodifiableMap(map);
    }

}


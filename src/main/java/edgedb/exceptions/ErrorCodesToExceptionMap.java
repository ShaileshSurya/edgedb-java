package edgedb.exceptions;

import edgedb.exceptions.clientexception.*;

import java.util.*;

public interface ErrorCodesToExceptionMap {

    public static final Map<Integer, BaseException> errorCodesMap = initMap();

    public static Map<Integer, BaseException> initMap() {
        Map<Integer, BaseException> map = new HashMap<>();
        map.put(0x01000000, new ClientException("InternalServerError"));

        map.put(0x02000000, new ClientException("UnsupportedFeatureError"));

        map.put(0x03000000, new ClientException("ProtocolError"));
        map.put(0x03010000, new ClientException("BinaryProtocolError"));
        map.put(0x03010001, new ClientException("UnsupportedProtocolVersionError"));
        map.put(0x03010002, new ClientException("TypeSpecNotFoundError"));
        map.put(0x03010003, new ClientException("UnexpectedMessageError"));
        map.put(0x03020000, new ClientException("InputDataError"));
        map.put(0x03030000, new ClientException("ResultCardinalityMismatchError"));
        map.put(0x03040000, new ClientException("CapabilityError"));
        map.put(0x03040100, new ClientException("UnsupportedCapabilityError"));
        map.put(0x03040200, new ClientException("DisabledCapabilityError"));


        map.put(0x04000000, new DBQueryException("QueryError"));

        map.put(0x04010000, new InvalidArgumentException("InvalidSyntaxError"));
        map.put(0x04010100, new InvalidArgumentException("EdgeQLSyntaxError"));
        map.put(0x04010200, new InvalidArgumentException("SchemaSyntaxError"));
        map.put(0x04010300, new InvalidArgumentException("GraphQLSyntaxError"));


        map.put(0x04030001, new UnknownArgumentException("UnknownModuleError"));
        map.put(0x04030002, new UnknownArgumentException("UnknownLinkError"));
        map.put(0x04030003, new UnknownArgumentException("UnknownPropertyError"));
        map.put(0x04030004, new UnknownArgumentException("UnknownUserError"));
        map.put(0x04030005, new UnknownArgumentException("UnknownDatabaseError"));
        map.put(0x04030006, new UnknownArgumentException("UnknownLinkError"));


        map.put(0x04040000, new UnknownArgumentException("SchemaError"));

        map.put(0x04050000, new UnknownArgumentException("SchemaDefinitionError"));

        map.put(0x04050100, new InvalidArgumentException("InvalidDefinitionError"));
        map.put(0x04050101, new InvalidArgumentException("InvalidModuleDefinitionError"));
        map.put(0x04050102, new InvalidArgumentException("InvalidLinkDefinitionError"));
        map.put(0x04050103, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put(0x04050104, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put(0x04050105, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put(0x04050106, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put(0x04050107, new InvalidArgumentException("InvalidAliasDefinitionError"));
        map.put(0x04050108, new UnknownArgumentException("InvalidDefinitionError"));
        map.put(0x04050109, new UnknownArgumentException("InvalidDefinitionError"));
        map.put(0x0405010A, new UnknownArgumentException("InvalidDefinitionError"));


        map.put(0x04050200, new InvalidArgumentException("DuplicateDefinitionError"));
        map.put(0x04050201, new InvalidArgumentException("DuplicateModuleDefinitionError"));
        map.put(0x04050202, new InvalidArgumentException("DuplicateLinkDefinitionError"));
        map.put(0x04050203, new InvalidArgumentException("DuplicatePropertyDefinitionError"));
        map.put(0x04050204, new InvalidArgumentException("DuplicateUserDefinitionError"));
        map.put(0x04050205, new InvalidArgumentException("DuplicateDatabaseDefinitionError"));
        map.put(0x04050206, new InvalidArgumentException("DuplicateOperatorDefinitionError"));
        map.put(0x04050207, new InvalidArgumentException("DuplicateViewDefinitionError"));
        map.put(0x04050208, new InvalidArgumentException("DuplicateFunctionDefinitionError"));
        map.put(0x004050209, new InvalidArgumentException("DuplicateConstraintDefinitionError"));
        map.put(0x0405020A, new InvalidArgumentException("DuplicateCastDefinitionError"));


        map.put(0x04060000, new DBQueryException("QueryTimeoutError"));


        map.put(0x05000000, new ClientException("ExecutionError"));
        map.put(0x05010000, new ClientException("InvalidValueError"));
        map.put(0x05010001, new ClientException("DivisionByZeroError"));
        map.put(0x05010002, new ClientException("NumericOutOfRangeError"));

        map.put(0x05020000, new ClientException("IntegrityError"));
        map.put(0x05020001, new ClientException("ConstraintViolationError"));
        map.put(0x05020002, new ClientException("CardinalityViolationError"));
        map.put(0x05020003, new ClientException("MissingRequiredError"));


        map.put(0x06000000, new ClientException("ConfigurationError"));

        map.put(0x07000000, new ClientException("AccessError"));
        map.put(0x07010000, new ClientException("AuthenticationError"));


        map.put(0xF0000000, new ClientException("LogMessage"));
        map.put(0xF0010000, new ClientException("WarningMessage"));

        return Collections.unmodifiableMap(map);
    }

}


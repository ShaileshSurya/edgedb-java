package edgedb.exceptions;

import edgedb.exceptions.clientexception.*;

import java.util.*;

public interface ErrorCodesToExceptionMap {

    public static final Map<Byte, BaseException> errorCodesMap = initMap();

    public static Map<Byte, BaseException> initMap() {
        Map<Byte, BaseException> map = new HashMap<>();
        map.put((byte) 01000000, new ClientException("InternalServerError"));

        map.put((byte) 02000000, new ClientException("UnsupportedFeatureError"));

        map.put((byte) 03000000, new ClientException("ProtocolError"));
        map.put((byte) 03010000, new ClientException("BinaryProtocolError"));
        map.put((byte) 03010001, new ClientException("UnsupportedProtocolVersionError"));
        map.put((byte) 03010002, new ClientException("TypeSpecNotFoundError"));
        map.put((byte) 03010003, new ClientException("UnexpectedMessageError"));
        map.put((byte) 03020000, new ClientException("InputDataError"));
        map.put((byte) 03030000, new ClientException("ResultCardinalityMismatchError"));
        map.put((byte) 03040000, new ClientException("CapabilityError"));
        map.put((byte) 03040100, new ClientException("UnsupportedCapabilityError"));
        map.put((byte) 03040200, new ClientException("DisabledCapabilityError"));


        map.put((byte) 04000000, new DBQueryException("QueryError"));

        map.put((byte) 04010000, new InvalidArgumentException("InvalidSyntaxError"));
        map.put((byte) 04010100, new InvalidArgumentException("EdgeQLSyntaxError"));
        map.put((byte) 04010200, new InvalidArgumentException("SchemaSyntaxError"));
        map.put((byte) 04010300, new InvalidArgumentException("GraphQLSyntaxError"));


        map.put((byte) 04030001, new UnknownArgumentException("UnknownModuleError"));
        map.put((byte) 04030002, new UnknownArgumentException("UnknownLinkError"));
        map.put((byte) 04030003, new UnknownArgumentException("UnknownPropertyError"));
        map.put((byte) 04030004, new UnknownArgumentException("UnknownUserError"));
        map.put((byte) 04030005, new UnknownArgumentException("UnknownDatabaseError"));
        map.put((byte) 04030006, new UnknownArgumentException("UnknownLinkError"));


        map.put((byte) 04040000, new UnknownArgumentException("SchemaError"));

        map.put((byte) 04050000, new UnknownArgumentException("SchemaDefinitionError"));

        map.put((byte) 04050100, new InvalidArgumentException("InvalidDefinitionError"));
        map.put((byte) 04050101, new InvalidArgumentException("InvalidModuleDefinitionError"));
        map.put((byte) 04050102, new InvalidArgumentException("InvalidLinkDefinitionError"));
        map.put((byte) 04050103, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put((byte) 04050104, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put((byte) 04050105, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put((byte) 04050106, new InvalidArgumentException("InvalidOperatorDefinitionError"));
        map.put((byte) 04050107, new InvalidArgumentException("InvalidAliasDefinitionError"));
        map.put((byte) 0x04050108, new UnknownArgumentException("InvalidDefinitionError"));
        map.put((byte) 0x04050109, new UnknownArgumentException("InvalidDefinitionError"));
        map.put((byte) 0x0405010A, new UnknownArgumentException("InvalidDefinitionError"));


        map.put((byte) 04050200, new InvalidArgumentException("DuplicateDefinitionError"));
        map.put((byte) 04050201, new InvalidArgumentException("DuplicateModuleDefinitionError"));
        map.put((byte) 04050202, new InvalidArgumentException("DuplicateLinkDefinitionError"));
        map.put((byte) 04050203, new InvalidArgumentException("DuplicatePropertyDefinitionError"));
        map.put((byte) 04050204, new InvalidArgumentException("DuplicateUserDefinitionError"));
        map.put((byte) 04050205, new InvalidArgumentException("DuplicateDatabaseDefinitionError"));
        map.put((byte) 04050206, new InvalidArgumentException("DuplicateOperatorDefinitionError"));
        map.put((byte) 04050207, new InvalidArgumentException("DuplicateViewDefinitionError"));
        map.put((byte) 0x04050208, new InvalidArgumentException("DuplicateFunctionDefinitionError"));
        map.put((byte) 0x04050209, new InvalidArgumentException("DuplicateConstraintDefinitionError"));
        map.put((byte) 0x0405020A, new InvalidArgumentException("DuplicateCastDefinitionError"));


        map.put((byte) 0x04060000, new DBQueryException("QueryTimeoutError"));


        map.put((byte) 0x05000000, new ClientException("ExecutionError"));
        map.put((byte) 0x05010000, new ClientException("InvalidValueError"));
        map.put((byte) 0x05010001, new ClientException("DivisionByZeroError"));
        map.put((byte) 0x05010002, new ClientException("NumericOutOfRangeError"));

        map.put((byte) 0x05020000, new ClientException("IntegrityError"));
        map.put((byte) 0x05020001, new ClientException("ConstraintViolationError"));
        map.put((byte) 0x05020002, new ClientException("CardinalityViolationError"));
        map.put((byte) 0x05020003, new ClientException("MissingRequiredError"));


        map.put((byte) 0x06000000, new ClientException("ConfigurationError"));

        map.put((byte) 0x07000000, new ClientException("AccessError"));
        map.put((byte) 0x07010000, new ClientException("AuthenticationError"));


        map.put((byte) 0xF0000000, new ClientException("LogMessage"));
        map.put((byte) 0xF0010000, new ClientException("WarningMessage"));

        return Collections.unmodifiableMap(map);
    }

}

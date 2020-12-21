package edgedb.internal.parser;

import edgedb.client.MovieEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class JsonProcessorTest {

    @Test
        public void unMarshalListTest() throws IOException {
        String listJson = "[{\"id\" : \"fea529a2-e383-11ea-ab9e-83991155abb0\", \"title\" : \"Blade Runner 2049\", \"year\" : 2017}, {\"id\" : \"065aab18-e384-11ea-ab9e-6f5a8baa466b\", \"title\" : \"Dune\", \"year\" : null}, {\"id\" : \"15533390-1856-11eb-8689-dfbf4004f5b4\", \"title\" : \"Dune\", \"year\" : null}, {\"id\" : \"1745e990-1856-11eb-98e9-37a0ff872504\", \"title\" : \"Dune\", \"year\" : null}, {\"id\" : \"40913868-1856-11eb-811a-9b60813706a4\", \"title\" : \"Shailesh_test\", \"year\" : null}, {\"id\" : \"41f320e0-1856-11eb-9922-c32815d7ef15\", \"title\" : \"Shailesh_test\", \"year\" : null}, {\"id\" : \"392d227c-18eb-11eb-bcbc-9fd8400f1618\", \"title\" : \"Shailesh_test\", \"year\" : null}, {\"id\" : \"3a9ccfc2-18eb-11eb-9206-f3e996bb214d\", \"title\" : \"Shailesh_test\", \"year\" : null}, {\"id\" : \"ed0c58bc-18f0-11eb-87e1-0f3896fc5ada\", \"title\" : \"Shailesh_test\", \"year\" : null}, {\"id\" : \"eeb48d10-18f0-11eb-9fe6-f7c60a338193\", \"title\" : \"Shailesh_test\", \"year\" : null}]";
        List<MovieEntity> list = JsonProcessor.unmarshalToList(listJson,MovieEntity.class);
        System.out.println(">>>>>>>MOVIEENTITY List>>>>>"+list);

    }


    @Test
    public void unMarshalTypeTest() throws IOException {
        String json = "{\"id\" : \"fea529a2-e383-11ea-ab9e-83991155abb0\", \"title\" : \"Blade Runner 2049\", \"year\" : 2017}";
        List<MovieEntity> list = JsonProcessor.unmarshalToList(json,MovieEntity.class);
        System.out.println(">>>>>>>MOVIEENTITY>>>>>"+json);
    }
}
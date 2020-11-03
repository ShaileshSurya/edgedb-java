package edgedb.client;

public interface Query {
    public String queryJSON();
    public String queryOneJSON();
    public String query();
    public String queryOne();
    public String execute();
}

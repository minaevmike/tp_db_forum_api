package dbService.executor;

import dbService.handlers.ResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SimpleExecutor {
	public void execQuery(Connection connection, String query, ResultHandler handler) throws SQLException
    {
		Statement stmt = connection.createStatement();
		stmt.execute(query);
		ResultSet result = stmt.getResultSet();
		handler.handle(result);
		result.close();
		stmt.close();
	}

	public void execUpdate(Connection connection, String update) throws SQLException
    {
		Statement stmt = connection.createStatement();
		stmt.execute(update);
		stmt.close();
	}

    public int execUpdateAndReturnId(Connection connection, String update) throws SQLException
    {
        Statement stmt = connection.createStatement();
        stmt.execute(update, Statement.RETURN_GENERATED_KEYS);

        ResultSet created = stmt.getGeneratedKeys();
        created.next();
        int id = created.getInt(1);

        stmt.close();
        return id;
    }
}

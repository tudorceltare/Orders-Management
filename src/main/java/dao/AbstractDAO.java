package dao;

import connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that implements the basic CRUD operations (create, read, update, delete) for a database table.
 * Methods are generic and use reflection to access the fields of the entity. Methods "create" and "updateById" use the
 * final object as a parameter. This class should be extended by all DAO classes.<br>
 * Method <b>create</b>: inserts an entity into the database using "<i>INSERT INTO tableName (fields) VALUES (values)</i>" query.<br>
 * Method <b>findById</b>: finds an entity in the database using "<i>SELECT * FROM tableName WHERE id = ?</i>" query.<br>
 * Method <b>findAll</b>: finds all entities in the database using " <i>SELECT * FROM tableName</i>" query.<br>
 * Method <b>updateById</b>: updates an entity in the database using
 *  "<i>UPDATE tableName SET field1 = ?, field2 = ?, ... WHERE id = ?</i>" query.<br>
 * Method <b>deleteById</b>: deletes an entity from the database using "<i>DELETE FROM tableName WHERE id = ?</i>" query.<br>
 * Method <b>getTableName</b>: returns the name of the table in the database.<br>
 * Method <b>mapToEntity</b>: maps the result set of a query to a list of entities.<br>
 * Method <b>camelCaseToSnakeCase</b>: converts a string from camel case to snake case.<br>
 * @param <T> The type of the entity
 */
public abstract class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    protected AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Inserts an entity into the database using "INSERT INTO tableName (fields) VALUES (values)" query. The fields are extracted
     * from the entity using reflection and the values are set as "?" in the query. The values are set using reflection.
     * @param entity The entity to be inserted into the database. The values are extracted using reflection.
     * @throws SQLException If the query fails
     * @throws IllegalAccessException If cannot access the fields of the entity
     */
    public void create(T entity) throws SQLException, IllegalAccessException {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        Field[] declaredFields = type.getDeclaredFields();
        for(Field field : declaredFields) {
            if (!field.getName().equals("id")) {
                if (fields.length() > 0) {
                    fields.append(", ");
                    values.append(", ");
                }
                fields.append(field.getName());
                values.append("?");
            }
        }

        String sqlQuery = "INSERT INTO " + getTableName() + " (" + fields + ") VALUES (" + values + ")";
        System.out.println(sqlQuery);
        LOGGER.info("Query before adding values: " + sqlQuery);

        try(Connection connection = ConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            int parameterIndex = 1;
            for(Field field : declaredFields) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(parameterIndex, field.get(entity));
                    parameterIndex++;
                }
            }
            LOGGER.info("Query after adding values: " + statement.toString());
            statement.executeUpdate();
        }
    }

    /**
     * Maps a ResultSet to an entity
     * @param id The id of the entity to be found
     * @return The entity with the given id
     * @throws SQLException If the query fails
     */
    public T findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        System.out.println(sql);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return mapToEntity(rs).get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds all the entities in the database using a "SELECT * FROM table_name" query
     * @return A list of all the entities in the database
     * @throws SQLException If the query fails
     */
    public List<T> findAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            return mapToEntity(rs);
        }
    }

    /**
     * Updates an entity in the database using a
     * "UPDATE table_name SET field1 = value1, field2 = value2, ... WHERE id = ?" query. The fields are extracted
     * from the entity using reflection and the values are set as "?" in the query. The values are set using reflection.
     * @param entity The entity to be updated in the database. The values are extracted using reflection.
     * @throws SQLException If the query fails
     * @throws IllegalAccessException If cannot access the fields of the entity
     */
    public void updateById(T entity) throws SQLException, IllegalAccessException {
        StringBuilder fields = new StringBuilder();
        StringBuilder sql = new StringBuilder("UPDATE " + getTableName() + " SET ");
        Field[] declaredFields = type.getDeclaredFields();
        String ending = " WHERE id = ";
        for(Field field : declaredFields) {
            if (!field.getName().equals("id")) {
                if (fields.length() > 0) {
                    fields.append(", ");
                }
                fields.append(field.getName()).append(" = ?");
            } else {
                field.setAccessible(true);
                ending += field.get(entity).toString();
            }
        }
        sql.append(fields);
        sql.append(ending);
        LOGGER.info("Query before adding values: " + sql);

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            for(Field field : declaredFields) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(parameterIndex, field.get(entity));
                    parameterIndex++;
                }
            }
            LOGGER.info("Query after adding values: " + statement.toString());
            statement.executeUpdate();
        }
    }

    /**
     * Deletes an entity from the database using a "DELETE FROM table_name WHERE id = ?" query
     * @param id The id of the entity to be deleted
     * @throws SQLException If the query fails
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteById " + e.getMessage());
        }
    }

    /**
     * Maps a ResultSet to a list of entities. The entities are created using reflection, the fields are extracted by
     * cycling through the fields of the entity class and the values are set using reflection.
     * @param rs The ResultSet to be mapped
     * @return A list of entities
     * @throws SQLException If the query fails
     */
    protected List<T> mapToEntity(ResultSet rs) throws SQLException {
        List<T> entities = new ArrayList<>();
        Constructor<?>[] constructors = type.getConstructors();
        Constructor<?> constructor = null;
        for (int i = 0; i < constructors.length; i++) {
            constructor = constructors[i];
            if (constructor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while(rs.next()) {
                constructor.setAccessible(true);
                T entity = (T) constructor.newInstance();
                Field[] fields = type.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    String snakeCaseFieldName = camelCaseToSnakeCase(fieldName);
                    Object value = rs.getObject(snakeCaseFieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(entity, value);
                }
                entities.add(entity);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException
                 | IntrospectionException | SQLException | SecurityException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    /**
     * This is a helper method when using Postgresql that returns the name of the table in the database. The name of
     * the table is the plural of the entity name, represented by the entity class in the model package, in lowercase.
     * For example, if the entity is "User", the table name is "users".
     * @return The name of the table in the database
     */
    protected String getTableName() {
        return type.getSimpleName().toLowerCase() + "s";
    }

    /**
     * This is a helper method when using Postgresql database. Postgresql uses snake_case for the column names, but the
     * java convention is camelCase. This method converts a camelCase string to a snake_case string.
     * @param camelCase The camelCase string to be converted
     * @return The snake_case string
     */
    protected String camelCaseToSnakeCase(String camelCase) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return camelCase.replaceAll(regex, replacement).toLowerCase();
    }
}

package model.dao.mapper;

import model.entities.Role;
import model.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements Mapper<User> {
    private final String id;
    private final String username;
    private final String password;
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final String roleId;

    public UserMapper(String id, String username, String password, String name, String phoneNumber,
                      String email, String roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roleId = roleId;
    }

    @Override
    public User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(id));
        user.setUsername(rs.getString(username));
        user.setPassword(rs.getString(password));
        user.setName(rs.getString(name));
        user.setPhoneNumber(rs.getString(phoneNumber));
        user.setEmail(rs.getString(email));
        user.setRole(Role.values()[rs.getInt(roleId)]);
        return user;
    }
}

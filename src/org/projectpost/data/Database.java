package org.projectpost.data;

import javafx.geometry.Pos;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static final String DATABASE_URL = "jdbc:sqlite:projectpost.db";

    private static Connection connection;

    public static void init() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);

        execute("CREATE TABLE IF NONE EXISTS projects (" +
                "uid char(36) PRIMARY KEY NOT NULL," +
                "name text NOT NULL," +
                "location int NOT NULL," +
                "time text NOT NULL," +
                "description text NOT NULL," +
                "image text NOT NULL," +
                "maxFunds int NOT NULL," +
                "currentFunds int NOT NULL" +
                ")"
        );


        execute("CREATE TABLE IF NOT EXISTS users (" +
                "uid char(36) PRIMARY KEY NOT NULL," +
                "name text NOT NULL," +
                "username text NOT NULL," +
                "passhash text NOT NULL," +
                "email text NOT NULL," +
                "phoneNumber text NOT NULL," +
                "address text NOT NULL" +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS volunteerPosts (" +
                "uid char(36) PRIMARY KEY NOT NULL," +
                "user char(36) NOT NULL," +
                "project char(36) NOT NULL," +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS donatePosts (" +
                "uid char(36) PRIMARY KEY NOT NULL," +
                "user char(36) NOT NULL," +
                "project char(36) NOT NULL," +
                "amount int NOT NULL," +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS postcards (" +
                "uid char(36) PRIMARY KEY NOT NULL," +
                "donation char(36) NOT NULL," +
                "message text NOT NULL," +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS sessions (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "user char(36) NOT NULL," +
                ")"
        );
    }

    private static void execute(String s) throws SQLException {
        Statement st = connection.createStatement();
        st.execute(s);
    }

    private static String genUID() {
        return UUID.randomUUID().toString();
    }

    public static ProjectData newProjectData() {
        ProjectData pd = new ProjectData();
        pd.uid = genUID();
        return pd;
    }

    public static ProjectData getProjectData(String uid) throws SQLException {
        String sql = "SELECT * FROM projects WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        ProjectData pd = new ProjectData();
        pd.uid = rs.getString(1);
        pd.name = rs.getString(2);
        pd.owner = rs.getString(3);
        pd.location = rs.getInt(4);
        pd.time = rs.getString(5);
        pd.description = rs.getString(6);
        pd.image = rs.getString(7);
        pd.maxFunds = rs.getInt(8);
        pd.currentFunds = rs.getInt(9);

        return pd;
    }

    public static void saveProjectData(ProjectData pd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projects VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pd.uid);
        stmt.setString(2, pd.name);
        stmt.setString(3, pd.owner);
        stmt.setInt(4, pd.location);
        stmt.setString(5, pd.time);
        stmt.setString(6, pd.description);
        stmt.setString(7, pd.image);
        stmt.setInt(8, pd.maxFunds);
        stmt.setInt(9, pd.currentFunds);
        stmt.execute();
    }



    public static UserData newUserData() {
        UserData ud = new UserData();
        ud.uid = genUID();
        return ud;
    }

    public static UserData getUserData(String uid) throws SQLException {
        String sql = "SELECT * FROM users WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        UserData ud = new UserData();
        ud.uid = rs.getString(1);
        ud.name = rs.getString(2);
        ud.username = rs.getString(3);
        ud.passhash = rs.getString(4);
        ud.email = rs.getString(5);
        ud.phoneNumber = rs.getString(6);
        ud.address = rs.getString(7);

        return ud;
    }

    public static void saveUserData(UserData ud) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projects VALUES (" +
                "?, ?, ?, ?, ?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, ud.uid);
        stmt.setString(2, ud.name);
        stmt.setString(3, ud.username);
        stmt.setString(4, ud.passhash);
        stmt.setString(5, ud.email);
        stmt.setString(6, ud.phoneNumber);
        stmt.setString(7, ud.address);
        stmt.execute();
    }

    public static boolean userExists(String uname) throws SQLException {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE username=? LIMIT 1)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uname);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        int exists = rs.getInt(1);

        return exists == 1;
    }

    public static UserData getUserByUsername(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        UserData ud = new UserData();
        ud.uid = rs.getString(1);
        ud.name = rs.getString(2);
        ud.username = rs.getString(3);
        ud.passhash = rs.getString(4);
        ud.email = rs.getString(5);
        ud.phoneNumber = rs.getString(6);
        ud.address = rs.getString(7);

        return ud;
    }



    public static VolunteerData newVolunteerData() {
        VolunteerData vd = new VolunteerData();
        vd.uid = genUID();
        return vd;
    }

    public static VolunteerData getVolunteerData(String uid) throws SQLException {
        String sql = "SELECT * FROM volunteerPosts WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        VolunteerData vd = new VolunteerData();
        vd.uid = rs.getString(1);
        vd.user = rs.getString(2);
        vd.project = rs.getString(3);

        return vd;
    }

    public static void saveVolunteerData(VolunteerData vd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projects VALUES (" +
                "?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, vd.uid);
        stmt.setString(2, vd.user);
        stmt.setString(3, vd.project);
        stmt.execute();
    }



    public static DonateData newDonateData() {
        DonateData dd = new DonateData();
        dd.uid = genUID();
        return dd;
    }

    public static DonateData getDonateData(String uid) throws SQLException {
        String sql = "SELECT * FROM donatePosts WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        DonateData dd = new DonateData();
        dd.uid = rs.getString(1);
        dd.user = rs.getString(2);
        dd.project = rs.getString(3);
        dd.amount = rs.getInt(4);

        return dd;
    }

    public static void saveDonateData(DonateData dd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projects VALUES (" +
                "?, ?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, dd.uid);
        stmt.setString(2, dd.user);
        stmt.setString(3, dd.project);
        stmt.setInt(4, dd.amount);
        stmt.execute();
    }



    public static PostcardData newPostcardData() {
        PostcardData pd = new PostcardData();
        pd.uid = genUID();
        return pd;
    }

    public static PostcardData getPostcardData(String uid) throws SQLException {
        String sql = "SELECT * FROM postcards WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        PostcardData pd = new PostcardData();
        pd.uid = rs.getString(1);
        pd.donation = rs.getInt(2);
        pd.message = rs.getString(3);

        return pd;
    }

    public static void savePostcardData(PostcardData pd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projects VALUES (" +
                "?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pd.uid);
        stmt.setInt(2, pd.donation);
        stmt.setString(3, pd.message);
        stmt.execute();
    }


    public static void newSession(String uid) throws SQLException {
        String sql = "INSERT INTO sessions VALUES (" +
                "?, ?" +
                ")";

        String newUID = genUID();
        newUID = newUID.replaceAll("-", "");

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newUID);
        stmt.setString(2, uid);
        stmt.execute();
    }

}


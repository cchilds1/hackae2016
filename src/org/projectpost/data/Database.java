package org.projectpost.data;

import javafx.geometry.Pos;
import sun.plugin.perf.PluginRollup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database {

    private static final String DATABASE_URL = "jdbc:sqlite:projectpost.db";

    private static Connection connection;

    public static void init() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);

        execute("CREATE TABLE IF NOT EXISTS projects (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "name text NOT NULL," +
                "owner char(32) NOT NULL," +
                "location int NOT NULL," +
                "time text NOT NULL," +
                "description text NOT NULL," +
                "image text NOT NULL," +
                "maxFunds int NOT NULL," +
                "currentFunds int NOT NULL" +
                ")"
        );


        execute("CREATE TABLE IF NOT EXISTS users (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "name text NOT NULL," +
                "username text NOT NULL," +
                "passhash text NOT NULL," +
                "email text NOT NULL," +
                "phoneNumber text NOT NULL," +
                "address text NOT NULL" +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS volunteerPosts (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "user char(32) NOT NULL," +
                "project char(32) NOT NULL" +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS donatePosts (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "user char(32) NOT NULL," +
                "project char(32) NOT NULL," +
                "amount int NOT NULL" +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS postcards (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "fromUser char(32) NOT NULL," +
                "toUser char(32) NOT NULL," +
                "project char(32) NOT NULL," +
                "message char(32) NOT NULL" +
                ")"
        );

        execute("CREATE TABLE IF NOT EXISTS sessions (" +
                "uid char(32) PRIMARY KEY NOT NULL," +
                "user char(32) NOT NULL" +
                ")"
        );

        String sql = "SELECT username FROM users";
        ResultSet rs = connection.createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    private static void execute(String s) throws SQLException {
        Statement st = connection.createStatement();
        st.execute(s);
    }

    private static String genUID() {
        String uid = UUID.randomUUID().toString().replaceAll("-", "");

        return uid;
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

        if (!rs.next()) {
            return null;
        }

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

        rs.close();

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

    public static List<ProjectData> getProjectsByUser(String userID) throws SQLException {
        List<ProjectData> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects WHERE owner = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, userID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
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

            projects.add(pd);
        }

        rs.close();

        return projects;
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

        rs.close();

        return ud;
    }

    public static void saveUserData(UserData ud) throws SQLException {
        String sql = "INSERT OR REPLACE INTO users VALUES (" +
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

        rs.close();

        return exists == 1;
    }

    public static UserData getUserByUsername(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, name);
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

        rs.close();

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

        if (!rs.next()) {
            return null;
        }

        VolunteerData vd = new VolunteerData();
        vd.uid = rs.getString(1);
        vd.user = rs.getString(2);
        vd.project = rs.getString(3);

        rs.close();

        return vd;
    }

    public static void saveVolunteerData(VolunteerData vd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO volunteerPosts VALUES (" +
                "?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, vd.uid);
        stmt.setString(2, vd.user);
        stmt.setString(3, vd.project);
        stmt.execute();
    }

    public static List<VolunteerData> getVolunteerDataForProject(String pid) throws SQLException {
        String sql = "SELECT * FROM volunteerPosts WHERE project = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pid);
        ResultSet rs = stmt.executeQuery();

        List<VolunteerData> volunteerDatas = new ArrayList<>();

        while (rs.next()) {
            VolunteerData vd = new VolunteerData();
            vd.uid = rs.getString(1);
            vd.user = rs.getString(2);
            vd.project = rs.getString(3);
            volunteerDatas.add(vd);
        }

        return volunteerDatas;
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

        if (!rs.next()) {
            return null;
        }

        DonateData dd = new DonateData();
        dd.uid = rs.getString(1);
        dd.user = rs.getString(2);
        dd.project = rs.getString(3);
        dd.amount = rs.getInt(4);

        rs.close();

        return dd;
    }

    public static void saveDonateData(DonateData dd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO donatePosts VALUES (" +
                "?, ?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, dd.uid);
        stmt.setString(2, dd.user);
        stmt.setString(3, dd.project);
        stmt.setInt(4, dd.amount);
        stmt.execute();
    }

    public static List<DonateData> getDonateDataForProject(String pid) throws SQLException {
        String sql = "SELECT * FROM donatePosts WHERE project = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pid);
        ResultSet rs = stmt.executeQuery();

        List<DonateData> donateDatas = new ArrayList<>();

        while (rs.next()) {
            DonateData dd = new DonateData();
            dd.uid = rs.getString(1);
            dd.user = rs.getString(2);
            dd.project = rs.getString(3);
            dd.amount = rs.getInt(4);
        }

        return donateDatas;
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

        if (rs.next()) {
            return null;
        }

        PostcardData pd = new PostcardData();
        pd.uid = rs.getString(1);
        pd.fromUser = rs.getString(2);
        pd.toUser = rs.getString(3);
        pd.project = rs.getString(4);
        pd.message = rs.getString(5);

        rs.close();

        return pd;
    }

    public static void savePostcardData(PostcardData pd) throws SQLException {
        String sql = "INSERT OR REPLACE INTO postcards VALUES (" +
                "?, ?, ?" +
                ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, pd.uid);
        stmt.setString(2, pd.fromUser);
        stmt.setString(3, pd.toUser);
        stmt.setString(4, pd.project);
        stmt.setString(5, pd.message);
        stmt.execute();
    }

    public static List<PostcardData> getPostcardsForUser(String uid) throws SQLException {
        List<PostcardData> postcards = new ArrayList<>();

        String sql = "SELECT * FROM postcards WHERE toUser=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            PostcardData pd = new PostcardData();
            pd.uid = rs.getString(1);
            pd.fromUser = rs.getString(2);
            pd.toUser = rs.getString(3);
            pd.project = rs.getString(4);
            pd.message = rs.getString(5);

            postcards.add(pd);
        }

        rs.close();

        return postcards;
    }



    public static String newSession(String uid) throws SQLException {
        String sql = "INSERT INTO sessions VALUES (" +
                "?, ?" +
                ")";

        String newUID = genUID();

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newUID);
        stmt.setString(2, uid);
        stmt.execute();
        return newUID;
    }

    public static String getSessionUserUid(String sessionID) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE uid=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, sessionID);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        String uid = rs.getString(2);

        rs.close();

        return uid;
    }


}


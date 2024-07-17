package main.translator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static String[] html(String s, boolean type) {
        // true -> English, false -> VN
        String search = type ? "select * from av where word = ?;"
                : "select * from av where description = ?";
        try (PreparedStatement searcher = HelloApplication.connection.prepareStatement(search)) {
            searcher.setString(1, s);
            ResultSet set = searcher.executeQuery();
            String[] res = new String[3];
            if (set.next()) {
                res[0] = set.getString("word");
                res[1] = set.getString("html");
                res[2] = set.getString("description");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> searchEnglish(String english) {
        List<String> res = new ArrayList<>();
        String search = "select * from av where word like ? limit 100;";
        try (PreparedStatement searcher = HelloApplication.connection.prepareStatement(search)) {
            searcher.setString(1, english + '%');
            ResultSet set = searcher.executeQuery();
            while (set.next()) {
                res.add(set.getString("word"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<String> searchVN(String vi) {
        List<String> res = new ArrayList<>();
        String search = "select * from av where description rlike ? limit 100;";
        try (PreparedStatement searcher = HelloApplication.connection.prepareStatement(search)) {
            searcher.setString(1, "\\b" + vi + "\\b");
            ResultSet set = searcher.executeQuery();
            while (set.next()) {
                res.add(set.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void insertDB(String word, String html, String meaning) throws SQLException {
        String insert = "insert into av(word, html, description)  value (?,?,?);";
        PreparedStatement inserter = HelloApplication.connection.prepareStatement(insert);
        inserter.setString(1, word);
        inserter.setString(2, html);
        inserter.setString(3, meaning);
        inserter.executeUpdate();
    }



    public static void removeDB(String word) throws SQLException {
        String insert = "delete from av where word = ?;";
        PreparedStatement inserter = HelloApplication.connection.prepareStatement(insert);
        inserter.setString(1, word);
        inserter.executeUpdate();
    }

    public static void updateDB(String word, String html, String meaning) throws SQLException {
        String insert = "update av set html = ?, description = ? where word = ?";
        PreparedStatement inserter = HelloApplication.connection.prepareStatement(insert);
        inserter.setString(3, word);
        inserter.setString(1, html);
        inserter.setString(2, meaning);
        inserter.executeUpdate();
    }

}

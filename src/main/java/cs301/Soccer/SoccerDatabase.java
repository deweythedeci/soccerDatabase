package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    private Hashtable<String, SoccerPlayer> database = new Hashtable<String, SoccerPlayer>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        if(getPlayer(firstName, lastName) != null){
            return false;
        }
        database.put(firstName + "\\" + lastName, new SoccerPlayer(firstName, lastName, uniformNumber, teamName));
        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        return database.remove(firstName + "\\" + lastName) != null;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        return database.get(firstName + "\\" + lastName);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        SoccerPlayer player = getPlayer(firstName, lastName);
        if(player == null){
            return false;
        }
        player.bumpGoals();
        return true;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        SoccerPlayer player = getPlayer(firstName, lastName);
        if(player == null){
            return false;
        }
        player.bumpYellowCards();
        return true;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        SoccerPlayer player = getPlayer(firstName, lastName);
        if(player == null){
            return false;
        }
        player.bumpRedCards();
        return true;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {

        //if teamName is for all players
        if(teamName == null){
            return database.size();
        }

        //gets a list of all the players in the database
        Enumeration<SoccerPlayer> players = database.elements();

        //iterates through the players and counts those with matching team names
        int num = 0;
        while(players.hasMoreElements()){
            if(players.nextElement().getTeamName().equals(teamName)){
                num++;
            }
        }
        return num;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {

        //holds the current index
        int currIdx = 0;

        //iterates through a list of all the players in the database
        Enumeration<SoccerPlayer> players = database.elements();
        SoccerPlayer player;
        while(players.hasMoreElements()){

            player = players.nextElement();

            //checks if the team name matches or team name is set to all
            if(teamName == null || player.getTeamName().equals(teamName)){

                //if index matches return player
                if(currIdx == idx){
                    return player;
                }

                currIdx++;
            }
        }

        //if player cannot be found returns null
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        /*try{
            //prints all player data to file
            PrintWriter pw = new PrintWriter(file);
            Enumeration<SoccerPlayer> players = database.elements();
            SoccerPlayer player;
            while(players.hasMoreElements()){
                player = players.nextElement();
                pw.println(player.getFirstName() + "\\" + player.getLastName() + "\\" +
                        player.getTeamName() + "\\" + player.getUniform() + "\\" +
                        player.getGoals() + "\\" + player.getYellowCards() + "\\" +
                        player.getRedCards());
            }
            return true;
        }
        catch(FileNotFoundException fe){
            return false;
        }*/
        return false;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}

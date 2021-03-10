package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
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
        try{
            //Scanner that looks for separating characters
            Scanner s = new Scanner(file);
            s.useDelimiter("[\\n\\\\]");

            //Declaring variables to hold information from each line
            SoccerPlayer player;
            String firstName;
            String lastName;
            String teamName;
            int uniformNum;
            int goals;
            int yellowCards;
            int redCards;

            //Goes line by line through the given file
            while(s.hasNext()){

                //Gets each piece of data from the current line
                firstName = s.next();
                lastName = s.next();
                teamName = s.next();
                uniformNum = Integer.parseInt(s.next());
                goals = Integer.parseInt(s.next());
                yellowCards = Integer.parseInt(s.next());
                redCards = Integer.parseInt(s.next());

                //Removes any duplicate players that may exist in the database
                if(database.contains(firstName + "\\" + lastName)){
                    removePlayer(firstName, lastName);
                }

                //Creates the new player
                player = new SoccerPlayer(firstName, lastName, uniformNum, teamName);

                //Initializes goals, yellow cards, and red cards
                for(int i = 0; i < goals; i++){
                    player.bumpGoals();
                }
                for(int i = 0; i < yellowCards; i++){
                    player.bumpYellowCards();
                }
                for(int i = 0; i < redCards; i++){
                    player.bumpRedCards();
                }

                //Adds the new player to the database
                database.put(firstName + "\\" + lastName, player);
            }
            s.close();

            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        try{
            //prints all player data to file
            PrintWriter pw = new PrintWriter(file);
            Enumeration<SoccerPlayer> players = database.elements();
            SoccerPlayer player;
            while(players.hasMoreElements()){
                player = players.nextElement();
                pw.println(logString(player.getFirstName() + "\\" + player.getLastName() + "\\" +
                        player.getTeamName() + "\\" + player.getUniform() + "\\" +
                        player.getGoals() + "\\" + player.getYellowCards() + "\\" +
                        player.getRedCards()));
            }
            pw.close();
            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
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
        HashSet<String> teams = new HashSet<>();
        Enumeration<SoccerPlayer> players = database.elements();
        String teamName;
        while(players.hasMoreElements()){
            teamName = players.nextElement().getTeamName();
            if(!teams.contains(teamName)){
                teams.add(teamName);
            }
        }
        return teams;
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

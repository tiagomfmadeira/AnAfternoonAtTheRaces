package sharedRegions;

// TODO: interface to exclude access from undesired entities
public class RaceTrack {

    //Horse Jockey
    public synchronized void proceedToStartLine(){}

    public synchronized void makeAMove(){}

    public synchronized boolean hasFinishLineBeenCrossed(){}


    //Broker
    public synchronized void startTheRace(int k){}
    
}

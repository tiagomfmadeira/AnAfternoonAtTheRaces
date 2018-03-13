package entities;

public class S
{
    void run ()
    {
        while(waitForNextRace())
        {
            goCheckHorses();
            placeABet();
            goWatchTheRace();

            if(haveIWon()) goCollectTheGains();
        }
        relaxABit();
    }
}

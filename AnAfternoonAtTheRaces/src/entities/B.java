package entities;

public class B
{
    void run ()
    {
        for(int i = 0; i < numRaces - 1; i++)
        {
            summonHorsesToPaddock();
            acceptTheBets();
            startTheRace();

            reportResults();

            if(areThereAnyWinners()) honourTheBets();

        }
    }
}

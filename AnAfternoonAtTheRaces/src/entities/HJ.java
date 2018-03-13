package entities;

public class HJ
{
    void run ()
    {
        proceedToStable();
        proceedToPaddock();
        proceedToStartLine();

        do
        {
            makeAMove();
        } while(!hasFinishLineBeenCrossed())

        proceedToStable();
    }
}
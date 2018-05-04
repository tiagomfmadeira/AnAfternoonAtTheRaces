package main;

import entities.Spectator;
import settings.Settings;
import stub.*;
import static main.SimulPar.M_numSpectators;

/**
 * General description: Main class.
 */
public class SpectatorMain
{

    /**
     * Creation and starting of the Spectator entity. Instantiation of the
     * required stubs.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        ControlCenterStub controlCenter = new ControlCenterStub(
                settings.CONTROL_CENTER_HOST_NAME,
                settings.CONTROL_CENTER_PORT_NUM
        );

        BettingCenterStub bettingCenter = new BettingCenterStub(
                settings.BETTING_CENTER_HOST_NAME,
                settings.BETTING_CENTER_PORT_NUM
        );

        PaddockStub paddock = new PaddockStub(
                settings.PADDOCK_HOST_NAME,
                settings.PADDOCK_PORT_NUM
        );

        Spectator spectator[] = new Spectator[M_numSpectators];

        for (int i = 0; i < M_numSpectators; i++)
        {
            int wallet = 1000;
            spectator[i] = new Spectator("spectator_" + i, i, wallet,
                    controlCenter, paddock, bettingCenter, gr);
            spectator[i].start();
        }

        for (int i = 0; i < M_numSpectators; i++)
        {
            try
            {
                spectator[i].join();
            } catch (InterruptedException e)
            {
            }
        }
        gr.shutdown();
    }
}
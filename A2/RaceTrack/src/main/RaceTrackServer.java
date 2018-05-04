package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.RaceTrack;
import stub.GeneralRepositoryStub;

/**
 * General description: Main class.
 */
public class RaceTrackServer
{

    /**
     * Instantiation of the Race Track shared memory region. Setting up of the
     * server awaiting requests inbound. Instantiation of service providing
     * agents, which will execute functions of the shared memory region.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        //Shared region
        RaceTrack rt = new RaceTrack(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.RACE_TRACK_PORT_NUM);
        scon.start();

        System.out.println("RaceTrack server is up!");
        System.out.println("RaceTrack server is listening...");

        /*
         * processamento de pedidos
         */
        while (!rt.hasServiceFinished())
        {
            sconi = scon.accept();              // entrar em processo de escuta
            if (sconi == null)
            {
                continue;
            }
            aps = new ServerThread(sconi, rt);  // lançar agente prestador de serviço
            aps.start();
        }

        scon.close();
    }
}

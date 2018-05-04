package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.BettingCenter;
import stub.GeneralRepositoryStub;

/**
 * General description: Main class.
 */
public class BettingCenterServer
{

    /**
     * Instantiation of the Betting Center shared memory region. Setting up of
     * the server awaiting requests inbound. Instantiation of service providing
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

        Settings set = gr.getSettings();

        //Shared region
        BettingCenter bc = new BettingCenter(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(set.BETTING_CENTER_PORT_NUM);
        scon.start();

        System.out.println("BettingCenter server is up!");
        System.out.println("BettingCenter server is listening...");

        /*
         * processamento de pedidos
         */
        while (!bc.hasServiceFinished())
        {
            sconi = scon.accept();              // entrar em processo de escuta
            if (sconi == null)
            {
                continue;
            }
            aps = new ServerThread(sconi, bc);  // lançar agente prestador de serviço
            aps.start();
        }
        scon.close();
    }
}

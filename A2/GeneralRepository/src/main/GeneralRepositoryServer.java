package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.GeneralRepository;

/**
 * General description: Main class.
 */
public class GeneralRepositoryServer
{

    /**
     * Instantiation of the General Repository shared memory region. Setting up
     * of the server awaiting requests inbound. Instantiation of service
     * providing agents, which will execute functions of the shared memory
     * region.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {

        ServerCom scon, sconi;

        scon = new ServerCom(Settings.GENERAL_REPOSITORY_PORT_NUM);
        scon.start();

        ServerThread aps;

        GeneralRepository gr = new GeneralRepository();

        System.out.println("GeneralRepository server is up!");
        System.out.println("GeneralRepository server is listening...");

        /*
         * processamento de pedidos
         */
        while (!gr.hasServiceFinished())
        {
            sconi = scon.accept();

            if (sconi != null)
            {
                aps = new ServerThread(sconi, gr);  // lançar agente prestador de serviço
                aps.start();
            }
        }

        scon.close();
    }
}

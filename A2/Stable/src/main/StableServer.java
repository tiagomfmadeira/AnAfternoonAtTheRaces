package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.Stable;
import stub.GeneralRepositoryStub;

/**
 * General description: Main class.
 */
public class StableServer
{

    /**
     * Instantiation of the Stable shared memory region. Setting up of the
     * server awaiting requests inbound. Instantiation of service providing
     * agents, which will execute functions of the shared memory region.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub generalRepositoryStub = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = generalRepositoryStub.getSettings();

        //Shared region
        Stable stable = new Stable(generalRepositoryStub);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.STABLE_PORT_NUM);
        scon.start();

        System.out.println("Stable server is up!");
        System.out.println("Stable server is listening...");

        /*
         * processamento de pedidos
         */
        while (!stable.hasServiceFinished())
        {
            // entrar em processo de escuta
            sconi = scon.accept();

            if (sconi != null)
            {
                aps = new ServerThread(sconi, stable);  // lançar agente prestador de serviço
                aps.start();
            }
        }
        scon.close();
    }
}

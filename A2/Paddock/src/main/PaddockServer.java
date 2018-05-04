package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.Paddock;
import stub.GeneralRepositoryStub;

/**
 * General description: Main class.
 */
public class PaddockServer
{

    /**
     * Instantiation of the Paddock shared memory region. Setting up of the
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
        Paddock p = new Paddock(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.PADDOCK_PORT_NUM);
        scon.start();

        System.out.println("Paddock server is up!");
        System.out.println("Paddock server is listening...");

        while (!p.hasServiceFinished())
        {
            sconi = scon.accept();
            if (sconi == null)
            {
                continue;
            }
            aps = new ServerThread(sconi, p);  // lançar agente prestador de serviço
            aps.start();
        }
        scon.close();
    }
}

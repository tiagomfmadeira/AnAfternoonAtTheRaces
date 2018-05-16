package main;

import interfaces.IControlCenter;
import settings.Settings;
import sharedRegions.ControlCenter;

/**
 * General description: Main class.
 */
public class ControlCenterServer
{

    /**
     * Instantiation of the Control Center shared memory region. Setting up of
     * the server awaiting requests inbound. Instantiation of service providing
     * agents, which will execute functions of the shared memory region.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        // get settings from general repository
        /*GeneralRepositoryStub generalRepositoryStub = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = generalRepositoryStub.getSettings();

        //Shared region
        IControlCenter IControlCenter = new ControlCenter(generalRepositoryStub);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.CONTROL_CENTER_PORT_NUM);
        scon.start();

        System.out.println("ControlCenter server is up!");
        System.out.println("ControlCenter server is listening...");

        /*
         * processamento de pedidos

        while (!IControlCenter.hasServiceFinished())
        {
            sconi = scon.accept();              // entrar em processo de escuta
            if (sconi == null)
            {
                continue;
            }
            aps = new ServerThread(sconi, IControlCenter);  // lançar agente prestador de serviço
            aps.start();
        }
        scon.close();*/
    }
}

package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.ControlCenter;
import stub.GeneralRepositoryStub;

public class ControlCenterServer
{

    public static void main(String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        //Shared region
        ControlCenter cc = new ControlCenter(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.CONTROL_CENTER_PORT_NUM);
        scon.start();

        System.out.println("ControlCenter server is up!");
        System.out.println("ControlCenter server is listening...");

        /*
         * processamento de pedidos
         */
        while (!cc.hasServiceFinished())
        {
            sconi = scon.accept();              // entrar em processo de escuta
            if (sconi == null)
            {
                continue;
            }
            aps = new ServerThread(sconi, cc);  // lançar agente prestador de serviço
            aps.start();
        }
        scon.close();
    }
}

package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.BettingCenter;
import sharedRegions.ControlCenter;
import stub.GeneralRepositoryStub;

public class ControlCenterServer {
    public static void main (String[] args)
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

        System.out.println("O CC server foi estabelecido!");
        System.out.println("O servidor CC esta em escuta.");

        /* processamento de pedidos */

        // temporary
        boolean hasServiceFinished = false;

        while (!hasServiceFinished)
        {
            sconi = scon.accept();              // entrar em processo de escuta
            aps = new ServerThread(sconi, cc);  // lançar agente prestador de serviço
            aps.start();
        }
    }
}

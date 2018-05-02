package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.BettingCenter;
import sharedRegions.Stable;
import stub.GeneralRepositoryStub;

public class StableServer {
    public static void main (String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        //Shared region
        Stable s = new Stable(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.STABLE_PORT_NUM);
        scon.start();

        System.out.println("O S server foi estabelecido!");
        System.out.println("O servidor S esta em escuta.");

        /* processamento de pedidos */

        // temporary
        boolean hasServiceFinished = false;

        while (!hasServiceFinished)
        {
            sconi = scon.accept();              // entrar em processo de escuta
            aps = new ServerThread(sconi, s);  // lançar agente prestador de serviço
            aps.start();
        }
    }
}

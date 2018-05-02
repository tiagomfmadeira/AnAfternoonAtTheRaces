package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.BettingCenter;
import sharedRegions.GeneralRepository;
import stub.BettingCenterStub;
import stub.GeneralRepositoryStub;

public class BettingCenterServer {

    public static void main (String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        //Shared region
        BettingCenter bc = new BettingCenter(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.BETTING_CENTER_PORT_NUM);
        scon.start();

        System.out.println("O BC server foi estabelecido!");
        System.out.println("O servidor BC esta em escuta.");

        /* processamento de pedidos */

        // temporary
        boolean hasServiceFinished = false;

        while (!hasServiceFinished)
        {
            sconi = scon.accept();              // entrar em processo de escuta
            aps = new ServerThread(sconi, bc);  // lançar agente prestador de serviço
            aps.start();
        }
    }
}

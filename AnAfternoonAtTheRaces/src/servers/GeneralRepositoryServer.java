package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.ControlCenter;
import sharedRegions.GeneralRepository;
import stub.BettingCenterStub;
import stub.GeneralRepositoryStub;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class GeneralRepositoryServer {

    public static void main (String[] args)
    {

        ServerCom scon, sconi;

        scon = new ServerCom(Settings.GENERAL_REPOSITORY_PORT_NUM);
        scon.start();

        ServerThread aps;

        GeneralRepository gr = new GeneralRepository();

        System.out.println("O GR server foi estabelecido!");
        System.out.println("O servidor GR esta em escuta.");

        /* processamento de pedidos */


        while (!gr.hasServiceFinished())
        {
            sconi = scon.accept();

            if(sconi != null) {
                // entrar em processo de escuta
                aps = new ServerThread(sconi, gr);  // lançar agente prestador de serviço
                aps.start();
            }
        }

        scon.close();
    }
}

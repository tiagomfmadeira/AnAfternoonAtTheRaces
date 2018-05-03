package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.GeneralRepository;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class GeneralRepositoryServer
{

    public static void main (String[] args)
    {

        ServerCom scon, sconi;

        scon = new ServerCom(Settings.GENERAL_REPOSITORY_PORT_NUM);
        scon.start();

        ServerThread aps;

        GeneralRepository gr = new GeneralRepository();

        System.out.println("GeneralRepository server is up!");
        System.out.println("GeneralRepository server is listening...");

        /* processamento de pedidos */


        while (!gr.hasServiceFinished())
        {
            sconi = scon.accept();

            if(sconi != null) {
                aps = new ServerThread(sconi, gr);  // lançar agente prestador de serviço
                aps.start();
            }
        }

        scon.close();
    }
}

package main;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.Paddock;
import stub.GeneralRepositoryStub;
import java.net.SocketTimeoutException;

public class PaddockServer
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
            if(sconi == null){ continue; }
            aps = new ServerThread(sconi, p);  // lançar agente prestador de serviço
            aps.start();
        }
        scon.close();
    }
}
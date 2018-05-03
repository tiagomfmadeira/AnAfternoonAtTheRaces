package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.BettingCenter;
import sharedRegions.RaceTrack;
import stub.GeneralRepositoryStub;

import java.net.SocketTimeoutException;

public class RaceTrackServer {
    public static void main (String[] args)
    {
        // get settings from general repository
        GeneralRepositoryStub gr = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        //Shared region
        RaceTrack rt = new RaceTrack(gr);

        // connection
        ServerCom scon, sconi;
        ServerThread aps;
        scon = new ServerCom(settings.RACE_TRACK_PORT_NUM);
        scon.start();

        System.out.println("O RT server foi estabelecido!");
        System.out.println("O servidor RT esta em escuta.");

        /* processamento de pedidos */

        // temporary
        boolean hasServiceFinished = false;

        while (!rt.hasServiceFinished())
        {
            sconi = scon.accept();
            if(sconi == null){ continue; }           // entrar em processo de escuta
            aps = new ServerThread(sconi, rt);  // lançar agente prestador de serviço
            aps.start();
        }

        scon.close();
    }
}

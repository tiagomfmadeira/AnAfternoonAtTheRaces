package servers;

import communication.ServerCom;
import settings.Settings;
import sharedRegions.ControlCenter;
import sharedRegions.GeneralRepository;

public class GeneralRepositoryServer {

    public static void main (String[] args)
    {
        ServerCom scon, sconi;

        scon = new ServerCom(Settings.GENERAL_REPOSITORY.getPortNumber());
        scon.start ();

        ServerThread aps;

        GeneralRepository logger = new GeneralRepository();

        System.out.println("O GR server foi estabelecido!");
        System.out.println("O servidor GR esta em escuta.");

        /* processamento de pedidos */


        // temporary
        boolean hasServiceFinished = false;

        while (!hasServiceFinished)
        {
            sconi = scon.accept();                  // entrar em processo de escuta
            aps = new ServerThread(sconi, logger);  // lançar agente prestador de serviço
            aps.start();
        }
    }
}

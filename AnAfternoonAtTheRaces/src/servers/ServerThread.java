package servers;

import communication.Message;
import communication.MessageType;
import communication.ServerCom;
import sharedRegions.BettingCenter;
import sharedRegions.GeneralRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ServerThread extends Thread{

    /**
     *  Variáveis internas
     */

    private ServerCom com;                                  // socket de comunicação com o cliente
    private Object region;                         // serviço a ser fornecido

    /**
     *  Construtor de variáveis.
     *
     *     @param com canal de comunicação
     *     @param region serviço
     */

    public ServerThread (ServerCom com, Object region)
    {
        this.com = com;
        this.region = region;
    }

    /**
     *  Operacionalidade
     */

    @Override
    public void run ()
    {

        /* prestação propriamente dita do serviço */
        Message msg  = (Message)com.readObject();

        // seu envio ao cliente
        com.writeObject(new Message(invokeMethod(msg)));

    }

    // lida com invocar o metodo correto de acordo com a mensagem
    private Object invokeMethod(Message msg){

        //Called method of sharedRegion class
        Method method = null;
        try {
            method = region.getClass().getMethod(msg.getFunc(), Arrays.stream(msg.getArgs()).map(Object::getClass).toArray(Class[]::new));
        }
        catch(NoSuchMethodException e){
            System.out.println("Method doesn't exist in shared region");
            System.exit(1);
        }

        // return value
        Object result = null;

        try {
            if(method.getReturnType().equals(Void.TYPE))
                method.invoke(region,Arrays.stream(msg.getArgs()).map(obj -> obj.getClass().cast(obj)).toArray());
            else
                result = method.invoke(region,Arrays.stream(msg.getArgs()).map(obj -> obj.getClass().cast(obj)).toArray());
        }
        catch(IllegalAccessException | InvocationTargetException e){
            System.out.println("Error in target invocation");
            System.exit(1);
        }
        return result;
    }
}

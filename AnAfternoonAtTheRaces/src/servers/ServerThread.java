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

        // not sure of this
        com.close();
    }

    // lida com invocar o metodo correto de acordo com a mensagem
    private Object invokeMethod(Message msg){

        //Called method of sharedRegion class
        Method method = null;

        try {
            System.out.println(msg.getFunc());
            if(msg.getArgs() == null)
                method = region.getClass().getMethod(msg.getFunc());
            else{
                method = region.getClass().getMethod(msg.getFunc(),
                            Arrays.stream(msg.getArgs())
                                    .map(Object::getClass)
                                    .map(cl -> cl == Integer.class ? int.class : cl).toArray(Class[]::new));
            }
        }
        catch(NoSuchMethodException e){
            System.out.println("Method doesn't exist in shared region");
            e.printStackTrace();
            System.exit(1);
        }

        // return value
        Object result = null;

        //function parameters
        Object[] params = msg.getArgs() != null ?
                Arrays.stream(msg.getArgs())
                        .map(obj ->  (obj == Integer.class ? int.class : obj).getClass().cast(obj))
                        .toArray() :
                null;

        try {
            if(method.getReturnType().equals(Void.TYPE))
                method.invoke(region, params);
            else
                result = method.invoke(region,params);
        }
        catch(IllegalAccessException | InvocationTargetException | NullPointerException e){
            System.out.println("Error in target invocation");
            e.printStackTrace();
            System.exit(1);

        }
        return result;
    }
}

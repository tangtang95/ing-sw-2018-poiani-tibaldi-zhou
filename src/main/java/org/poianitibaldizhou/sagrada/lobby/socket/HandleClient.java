package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyServerController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Response;
import org.poianitibaldizhou.sagrada.lobby.socket.proxyviews.LobbyObserverProxyView;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleClient implements Runnable {

    private Socket socket;
    private ILobbyServerController controller;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private static final Logger LOGGER = Logger.getLogger(HandleClient.class.getName());

    public HandleClient(Socket socket, ILobbyServerController controller) {
        this.socket = socket;
        this.controller = controller;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public void sendResponse(Object response){
        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Object object = null;
        do {
            try {
                object = objectInputStream.readObject();
                if (object instanceof Request) {
                    Request request = (Request) object;
                    //replaceObserver(request);
                    Object reply = request.invokeMethod(controller);
                    if(reply != null)
                        sendResponse(new Response(reply));
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                object = null;
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, e.toString());
                object = null;
            }
        } while (object != null);
        close();
    }

    public void replaceObserver(Request request){
        List<Object> parameters = request.getMethodParameters();
        for (int i = 0; i < parameters.size(); i++) {
            if(parameters.get(i) instanceof ILobbyObserver){
                request.replaceParameter(new LobbyObserverProxyView(this,
                        (ILobbyObserver) parameters.get(i)), i);
            }
            if(parameters.get(i) instanceof ILobbyView){

            }
        }
    }

}

package org.example;

public class ClientChatProtocol {
    private volatile String myString;
    public void setString(String value){this.myString = value;}
    public String getString(){
        return myString;
    }
}

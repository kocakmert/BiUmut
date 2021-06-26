package com.example.blood;

public class Class_DataBase_Connection {
    private static Class_DataBase_Connection dbc = null;
    private static String CONNECTION;
    private Class_DataBase_Connection() {
        CONNECTION="http://alperoner.com/kocak/blood/";

        //SONRADAN BAŞKA BİR VERİ TABANI BAĞLANTI TEKNİĞİ KULLANILABİLİR...
        // ?? Geliştirilebilir ??
    }
    public static Class_DataBase_Connection getDataBaseCon(){
        if(dbc == null){
            dbc=new Class_DataBase_Connection();
        }
        return dbc;
    }
    public String getCONNECTION() {
        return CONNECTION;
    }
}

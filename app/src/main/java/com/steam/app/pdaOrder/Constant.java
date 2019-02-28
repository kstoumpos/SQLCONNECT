package com.steam.app.pdaOrder;

public class Constant {

    public static final int PRODUCTYPE_SIMPLE = 11;
    public static final int PRODUCTYPE_SINTHESI = 12;
    public static final int PRODUCTYPE_BASH_PAR = 13;
    public static final int PRODUCTYPE_YLIKO_PAR = 14;
    public static final int PRODUCTYPE_SYNTAGH = 15;
    public static final int PRODUCTYPE_SET_EIDWN = 16;
    public static final int PRODUCTYPE_YPHRESIA = 17;
    public static final int PRODUCTYPE_SET_EIDWN_2LEVEL = 160;
    public static final int PRODUCTYPE_EXTRA_EPILOGES = 180;
    public static final int PRODUCTYPE_MANUAL_COMENTS = 181;

    public enum enmTBLstate {
        EMPTY,
        IDLE,
        NEWORDER,
        CLOSEORDER
    }

    public enum enmPdaOrderState {
        ORDER_justARRIVED,
        ORDER_isPRINTED,
        ORDER_isREADY,
        RECEIPT_PRINTED,
        PAYED,
        ORDER_CANCELD
    }
}

package se.frand.app.morsecodetranslator;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by victorfrandsen on 9/17/15.
 * really good handler video here:
 * https://www.youtube.com/watch?v=GaO1uHeIcj0
 */
public class Translator implements Runnable{

    public static final int MS_UNIT = 200;
    // length in ms of morse code actions
    public static final int MS_DOT_ON = MS_UNIT;
    public static final int MS_DASH_ON = MS_UNIT*3;
    public static final int MS_DOT_OFF = MS_UNIT; //for between dots and dashes
    public static final int MS_DASH_OFF = MS_UNIT*3;
    public static final int MS_PAUSE_OFF = MS_UNIT*7;

    // int codes for characters of morse code.
    public static final int OFF = 0;
    public static final int DOT = 1;
    public static final int DASH = 2;
    public static final int DASH_OFF = 3;
    public static final int PAUSE_OFF = 4;


    Handler mHandler;
    String missive;

    // to differentiate from the message objects which we'll be using
    // we are calling the string to translate "missive"
    public Translator (String str, Handler handler) {
        mHandler = handler;
        missive = str;
    }

    private void translate(String missive) {
        missive = missive.toUpperCase();
        Log.v("TRANSLATE", "Translating with missive " + missive);
        for(int c=0;c<missive.length();c++) {
            char ch = missive.charAt(c);
            int val = Character.getNumericValue(ch);
            if( val >= 10 && val <= 36) {
                int[] array = CODES_AZ[val-10];
                for(int i=0;i<array.length;i++){
                    //blink each dash and dot
                    blink(array[i],ch);
                }
            } else if(val >=0 && val <= 9) {
                int[] array = CODES_09[val];
                for(int i=0;i<array.length;i++){
                    //blink each dash and dot
                    blink(array[i],ch);
                }
            } else if(val==32) {
                //blink a MS_PAUSE_OFF length of black
                blink(PAUSE_OFF);
            } else {
                //no characters for it
                Log.e("TRANSLATOR", "Bad Character " + val + " " + ch );
                continue;
            }
            blink(DASH_OFF);
        }
    }
    private void blink(int signal) {
        int c = Character.getNumericValue('_');
        blink(signal,c);
    }

    private void blink(int signal, int codepoint) {
        Log.v("TRANSLATOR", "blinking");
        Message message = Message.obtain();
        message.arg2 = codepoint;
        int duration = 0;
        switch (signal) {
            case OFF:
                message.arg1 = Color.BLACK;
                duration = MS_DOT_OFF;
                break;
            case DOT:
                message.arg1 = Color.WHITE;
                duration = MS_DOT_ON;
                break;
            case DASH:
                message.arg1 = Color.WHITE;
                duration = MS_DASH_ON;
                break;
            case DASH_OFF:
                message.arg1 = Color.BLACK;
                duration = MS_DASH_OFF;
                break;
            default:
                message.arg1 = Color.BLACK;
                duration = MS_PAUSE_OFF;
        }
        mHandler.sendMessage(message);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.v("TRANSLATOR", "Starting run");
        translate(missive);
    }


    private static final int[][] CODES_AZ = {
            {DOT,OFF,DASH},//A
            {DASH,OFF,DOT,OFF,DOT,OFF,DOT},//B
            {DASH,OFF,DOT,OFF,DASH,OFF,DOT},//C
            {DASH,OFF,DOT,OFF,DOT},
            {DOT},//E
            {DOT,OFF,DOT,OFF,DASH,OFF,DOT},//F
            {DASH,OFF,DASH,OFF,DOT},//G
            {DOT,OFF,DOT,OFF,DOT,OFF,DOT},
            {DOT,OFF,DOT},
            {DOT,OFF,DASH,OFF,DASH,OFF,DASH},
            {DASH,OFF,DOT,OFF,DASH},//K
            {DOT,OFF,DASH,OFF,DOT,OFF,DOT},
            {DASH,OFF,DASH},
            {DASH,OFF,DOT},
            {DASH,OFF,DASH,OFF,DASH},
            {DOT,OFF,DASH,OFF,DASH,OFF,DOT},
            {DASH,OFF,DASH,OFF,DOT,OFF,DASH},
            {DOT,OFF,DASH,OFF,DOT},
            {DOT,OFF,DOT,OFF,DOT},
            {DASH},
            {DOT,OFF,DOT,OFF,DASH},
            {DOT,OFF,DOT,OFF,DOT,OFF,DASH},
            {DOT,OFF,DASH,OFF,DASH},
            {DASH,OFF,DOT,OFF,DOT,OFF,DASH},
            {DASH,OFF,DOT,OFF,DASH,OFF,DASH},
            {DASH,OFF,DASH,OFF,DOT,OFF,DOT}
    };
    private static final int[][] CODES_09 = {
            {DOT,OFF,DASH,OFF,DASH,OFF,DASH,OFF,DASH},
            {DOT,OFF,DOT,OFF,DASH,OFF,DASH,OFF,DASH},
            {DOT,OFF,DOT,OFF,DOT,OFF,DASH,OFF,DASH},
            {DOT,OFF,DOT,OFF,DOT,OFF,DOT,OFF,DASH},
            {DOT,OFF,DOT,OFF,DOT,OFF,DOT,OFF,DOT},
            {DASH,OFF,DOT,OFF,DOT,OFF,DOT,OFF,DOT},
            {DASH,OFF,DASH,OFF,DOT,OFF,DOT,OFF,DOT},
            {DASH,OFF,DASH,OFF,DASH,OFF,DOT,OFF,DOT},
            {DASH,OFF,DASH,OFF,DASH,OFF,DASH,OFF,DOT},
            {DASH,OFF,DASH,OFF,DASH,OFF,DASH,OFF,DASH}
    };
}

package se.frand.app.morsecodetranslator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    RelativeLayout messageField;
    TextView letter;
    Thread thread;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide the action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        setContentView(R.layout.activity_main);

        messageField = (RelativeLayout) findViewById(R.id.container);

        letter = (TextView) findViewById(R.id.letter);

        messageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDialog();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.v("MAIN", "Received Message");
                messageField.setBackgroundColor(msg.arg1);
                if(msg.arg2 >= 0)
                    letter.setText("" + (char)msg.arg2);
                else
                    letter.setText("");
            }
        };
    }


    private void newDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Translate Missive into Morse code:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Translate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // hide the begin message
                TextView textView = (TextView) findViewById(R.id.begin);
                textView.setVisibility(View.INVISIBLE);
                // begin translating
                String t =input.getText().toString();
                Log.v("MAIN","Creating new thread with missive " + t);
                thread = new Thread(new Translator(t, handler));
                thread.start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

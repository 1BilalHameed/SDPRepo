package edu.gatech.setclass.sdpencryptor;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SDPEncryptorActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText messageEditText;
    private EditText keyPhraseEditText;
    private EditText shiftNumberEditText;
    private Button encodeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdpencryptor);


        messageEditText = findViewById(R.id.entryTextID);
        keyPhraseEditText = findViewById(R.id.argInput1ID);
        shiftNumberEditText = findViewById(R.id.argInput2ID);
        resultTextView = findViewById(R.id.textEncryptedID);
        encodeButton = findViewById(R.id.encryptButtonID);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encodeMessage();
            }
        });
    }



    private void encodeMessage() {
        String entryText = messageEditText.getText().toString().trim();
        String arg1Text = keyPhraseEditText.getText().toString().trim();
        String arg2Text = shiftNumberEditText.getText().toString().trim();

        // Check for input validity
        if (entryText.isEmpty() || !entryText.matches(".*[a-zA-Z0-9].*")) {
            messageEditText.setError("Invalid Entry Text");
            resultTextView.setText("");
            return;
        }

        if (arg1Text.isEmpty()){
            keyPhraseEditText.setError("Invalid Arg Input 1");
            resultTextView.setText("");
            return;
        }
        if (arg2Text.isEmpty()){
            shiftNumberEditText.setError("Invalid Arg Input 2");
            resultTextView.setText("");
            return;
        }

        int arg1 = Integer.parseInt(arg1Text);
        int arg2 = Integer.parseInt(arg2Text);

        if (!isValidArg1(arg1)) {
            keyPhraseEditText.setError("Invalid Arg Input 1");
            resultTextView.setText("");
            return;
        }

        if (!isValidArg2(arg2)) {
            shiftNumberEditText.setError("Invalid Arg Input 2");
            resultTextView.setText("");
            return;
        }

        // Perform encryption
        String encryptedText = encrypt(entryText, arg1, arg2);
        resultTextView.setText("Text Encrypted = " + encryptedText);
    }

    private boolean isValidArg1(int arg1) {
        // Check if arg1 is coprime to 62 (i.e., gcd(arg1, 62) == 1)
        // Implement this validation logic

        int gcd = calculateGCD(arg1, 62);
        return gcd == 1;
    }

    private boolean isValidArg2(int arg2) {
        // Check if arg2 is within the valid range (1 to 61)
        return arg2 >= 1 && arg2 < 62;
    }

    private String encrypt(String text, int arg1, int arg2) {
        StringBuilder encryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                int x;

                if (Character.isLowerCase(c)) {
                    x = c - 'a'; // 0 to 25
                } else if (Character.isUpperCase(c)) {
                    x = c - 'A' + 26; // 26 to 51
                } else {
                    x = c - '0' + 52; // 52 to 61
                }

                int encryptedValue = (arg1 * x + arg2) % 62;
                char encryptedChar;

                if (encryptedValue < 26) {
                    encryptedChar = (char) (encryptedValue + 'a');
                } else if (encryptedValue < 52) {
                    encryptedChar = (char) (encryptedValue - 26 + 'A');
                } else {
                    encryptedChar = (char) (encryptedValue - 52 + '0');
                }

                encryptedText.append(encryptedChar);
            } else {
                // Non-alphanumeric characters remain unchanged
                encryptedText.append(c);
            }
        }

        return encryptedText.toString();
    }

    private int calculateGCD(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return calculateGCD(b, a % b);
        }
    }


}

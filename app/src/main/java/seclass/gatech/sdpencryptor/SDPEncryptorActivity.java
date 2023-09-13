package seclass.gatech.sdpencryptor;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.TextUtils;
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


        messageEditText = findViewById(R.id.messageInput);
        keyPhraseEditText = findViewById(R.id.keyPhrase);
        shiftNumberEditText = findViewById(R.id.shiftNumber);
        resultTextView = findViewById(R.id.cipherText);
        encodeButton = findViewById(R.id.encryptButton);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encodeMessage();
            }
        });
    }

    private void encodeMessage() {
        String message = messageEditText.getText().toString().trim();
        String keyPhrase = keyPhraseEditText.getText().toString().trim();
        String shiftNumberString = shiftNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(message) || !message.matches(".*[a-zA-Z]+.*")) {
            messageEditText.setError("Message must contain letters");
            resultTextView.setText("");
            return;
        }

        if (TextUtils.isEmpty(keyPhrase) || !keyPhrase.matches("[a-zA-Z]+")) {
            keyPhraseEditText.setError("Key Phrase must contain only letters");
            resultTextView.setText("");
            return;
        }

        int shiftNumber;
        try {
            shiftNumber = Integer.parseInt(shiftNumberString);
        } catch (NumberFormatException e) {
            shiftNumberEditText.setError("Shift Number must be >= 1");
            resultTextView.setText("");
            return;
        }

        if (shiftNumber < 1) {
            shiftNumberEditText.setError("Shift Number must be >= 1");
            resultTextView.setText("");
            return;
        }

        String encryptedMessage = encryptMessage(message, keyPhrase, shiftNumber);
        resultTextView.setText(encryptedMessage);
    }

    public static String encryptMessage(String message, String keyPhrase, int shiftNumber) {
        StringBuilder vigenereCipher = new StringBuilder();
        int keyIndex = 0;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);

            if (Character.isLetter(c)) {
                char keyChar = Character.toUpperCase(keyPhrase.charAt(keyIndex % keyPhrase.length()));
                int shift = Character.toUpperCase(keyChar) - 'A';

                if (Character.isLowerCase(c)) {
                    shift = (shift + 26) % 26;
                }

                char encryptedChar = (char) (((c - 'A' + shift) % 26) + 'A');
                vigenereCipher.append(encryptedChar);
                keyIndex++;
            } else {
                vigenereCipher.append(c);
            }
        }

        StringBuilder rotatedMessage = new StringBuilder();

        for (char c : vigenereCipher.toString().toCharArray()) {
            if (Character.isLetter(c)) {
                char rotatedChar = rotateCharacter(c, shiftNumber);
                if (c == '&') {
                    rotatedChar = '&';
                } else if (c == ' ') {
                    rotatedChar = '_';
                }
                rotatedMessage.append(rotatedChar);
            } else {
                rotatedMessage.append(c);
            }
        }

        return rotatedMessage.toString();
    }

    private static char rotateCharacter(char c, int shiftNumber) {
        char base = Character.isUpperCase(c) ? 'A' : 'a';
        int shifted = ((c - base) + shiftNumber) % 26;

        return (char) (base + shifted);
    }

}

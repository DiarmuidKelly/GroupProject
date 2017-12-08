package com.homecare.VCA.viewHolder;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.homecare.VCA.R;
import com.homecare.VCA.models.Message;
import com.homecare.VCA.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;


public class MessageBoardsActivity extends BaseActivity {

    private static final String TAG = MessageBoardsActivity.class.getName();

    private Button newMessageButton;
    private EditText newMessageEditText;
    private ListView listViewMessages;
    private Spinner spinnerPatients;
    private MessagesArrayAdapter adapter;

    private ArrayList<Message> messages;
    private Message[] arrayMessages;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_boards);

        spinnerPatients = (Spinner) findViewById(R.id.spinnerPatients);
        listViewMessages = (ListView) findViewById(R.id.listViewMessages);
        newMessageEditText = (EditText) findViewById(R.id.editTextNewMessage);
        newMessageButton = (Button) findViewById(R.id.buttonNewMessage);

        mFirestore = FirebaseFirestore.getInstance();

        updateUI();

    }

    private void updateUI(){

        // Get names of patients to fill spinner
        final User[] arrayUsers = new User[localUser.getPatientIds().size()];
        final String[] arrayPatientNames = new String[localUser.getPatientIds().size()];
        localUser.getPatients().toArray(arrayUsers);

        for(int i = 0;i<arrayUsers.length;i++){
            arrayPatientNames[i] = arrayUsers[i].getFirstName() + " " + arrayUsers[i].getLastName();
        }

        ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_entry, arrayPatientNames);

        spinnerPatients.setAdapter(namesAdapter);
        spinnerPatients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startReceivingMessagesFromDatabase(arrayUsers[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Get messages from firebase here
        messages = new ArrayList<Message>();
        adapter = new MessagesArrayAdapter(getApplicationContext(), messages);
        listViewMessages.setAdapter(adapter);

    }

    private void startReceivingMessagesFromDatabase(User patient){
        // Get additional information from database
        mFirestore.collection("messageBoards")
                .document(patient.getMessageBoardId())
                .collection("messages")
                .orderBy("sent")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        messages.clear();

                        for (DocumentSnapshot doc : snapshot) {
                            Message m = new Message(
                                    (String) doc.getData().get("fromUserName"),
                                    (Date) doc.getData().get("sent"),
                                    (String) doc.getData().get("msgText"));

                            messages.add(m);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private class MessagesArrayAdapter extends ArrayAdapter<Message> {
        private final Context context;
        private final ArrayList<Message> values;

        public MessagesArrayAdapter(Context context, ArrayList<Message> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Message currentMsg = values.get(position);
            // create custom layout for each list item
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_view_item_message, parent, false);

            TextView username = rowView.findViewById(R.id.textViewUserName);
            TextView message = rowView.findViewById(R.id.textViewMessage);
            TextView date = rowView.findViewById(R.id.textViewMessageDate);

            username.setText(currentMsg.getSentFrom());
            message.setText(currentMsg.getMessageText());
            date.setText(currentMsg.getSentOnDate().toString());

            return rowView;
        }
    }
}

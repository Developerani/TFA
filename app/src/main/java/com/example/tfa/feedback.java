package com.example.tfa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class feedback extends Fragment {

    EditText name,feedback;
    Button submit;

    public feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_feedback, container, false);
        name = v.findViewById(R.id.text_name);
        feedback = v.findViewById(R.id.feedback);
        submit = v.findViewById(R.id.button_feedback);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_v = name.getText().toString();
                String feedback_v = feedback.getText().toString();
                Toast.makeText(getContext(), name_v+" "+feedback_v, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
